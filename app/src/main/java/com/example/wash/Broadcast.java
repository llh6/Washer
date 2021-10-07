package com.example.wash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.wash.utils.Device;
import com.example.wash.utils.Util;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;

public class Broadcast {
    public static final String payMessage = "0224009B4203000300050150A8534B01000000300000000000000000000000000000000000000003";
    public static final String startMessage = "00001711";
    public int index=0;
    public DrawerLayout drawerLayout;
    public String content;
    public UsbDevice device_lora;
    public UsbDevice device_card;
    public UsbManager usbManager;
    public static UsbSerialPort port_lora;
    public UsbSerialPort port_card;
    public SerialInputOutputManager usbIoManager_card;
    public SerialInputOutputManager usbIoManager_lora;
    public List<UsbSerialDriver> drivers;
    public static final String ACTION_USB_PERMISSION = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public AppBarConfiguration mAppBarConfiguration;
    public String busyState="03",idleState="04",State="";
    public Broadcast() {
    }
    //发送测试
    public static void send_test() {
        int p=0;
        byte []tempData=new byte[2];
        byte []Data=new byte[6];
        Data[p++]=0;
        Data[p++]=strToByte(Device.getAddress());//当Address为null 崩溃
        Data[p++]=strToByte(Device.getChannel());
        Data[p++]='(';
        Data[p++]=0;
        tempData[0]=Data[p-1];
        Data[p++]=1;
        tempData[1]=Data[p-1];
        Data=byteMerger(Data, Util.hexStr2Byte(getCRC(tempData)));
        Data=byteMerger(Data,Util.toByteArray2(")"));
        send_lora(Data);
    }
    //轮询
    public void polling_wash() {
        byte Ch=1;
        for(byte i=0;i<2;i++)
        {
            send_polling(i,Ch);
        }
    }
    //初始化USB
    /*public void initUsbSerial() {
        // 1.查找设备
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // System.out.println("aaaaaaaaaaaaaaaa");
        drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (drivers.size() <= 0) {
           // Toast.makeText(this, "无串口设备", Toast.LENGTH_SHORT).show();
            return;
        }

        for (UsbSerialDriver driver : drivers) {
            int id = driver.getDevice().getProductId();
            if(id == 8963){
                device_lora = driver.getDevice();
                port_lora = driver.getPorts().get(0);
            }else if(id == 29987) {
                device_card = driver.getDevice();
                port_card = driver.getPorts().get(0);
            }
        }


        try {
            if (usbManager.hasPermission(device_lora) && usbManager.hasPermission(device_card)) {
                openLora();
                openCard();
            } else {
                Log.e("TAG", "没有权限");
                Broadcast.UsbPermissionActionReceiver mUsbPermissionActionReceiver = new Broadcast.UsbPermissionActionReceiver();
                Intent intent = new Intent(ACTION_USB_PERMISSION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
                registerReceiver(mUsbPermissionActionReceiver, intentFilter);
                usbManager.requestPermission(device_lora, pendingIntent);
                usbManager.requestPermission(device_card, pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "差一个设备", Toast.LENGTH_SHORT).show();
        }
    }*/


/*    public void openLora(){
        try {
            UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(device_lora);
            port_lora.open(usbDeviceConnection);
            port_lora.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            //bt_connect2.setText("Close");
            //Toast.makeText(MainActivity.this, "LORA", Toast.LENGTH_SHORT).show();
            InitRead_Lora();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsbInterface anInterface = device_lora.getInterface(0);

        if (anInterface == null) {
            //Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        //数据处理
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
//                    while(true)
//                    {
//                        DataProcessing();
//                    }
//               // DataProcessing();
//            }
//        }).start();

    }

    public void openCard(){
        try {
            UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(device_card);
            port_card.open(usbDeviceConnection);
            port_card.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            //Toast.makeText(MainActivity.this, "CARD", Toast.LENGTH_SHORT).show();
            InitRead_Card();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsbInterface anInterface = device_card.getInterface(0);
        if (anInterface == null) {
            //Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }*/

    //接受Lora信息
    /*public void InitRead_Lora() {
        usbIoManager_lora = new SerialInputOutputManager(port_lora, new SerialInputOutputManager.Listener() {
            @Override
            public void onNewData(byte[] data) {
                State = Util.bytesToHex(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(State);
                        //Toast.makeText(MainActivity.this, "收到成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onRunError(Exception e) {
            }
        });
        Executors.newSingleThreadExecutor().submit(usbIoManager_lora);
    }*/

    //接收校园卡返回的信息
    /*public void InitRead_Card() {
        usbIoManager_card = new SerialInputOutputManager(port_card, new SerialInputOutputManager.Listener() {
            @Override
            public void onNewData(byte[] data) {
                String s = Util.str2HexStr(new String(data));
                System.out.println(s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(s != null){
                            if(s.contains("030100"))  //030100
                            {
                                System.out.println("11111111111111111");
                                Toast.makeText(MainActivity.this, "开始支付", Toast.LENGTH_SHORT).show();
                                send_card(payMessage);
                            }else if(s.contains("06010000"))
                            {
                                Toast.makeText(MainActivity.this, "刷卡成功", Toast.LENGTH_SHORT).show();
                                //send_lora("HELLO"+ index);//发送内容

                            }
                        }
                    }
                });
            }
            @Override
            public void onRunError(Exception e) {
            }
        });
        Executors.newSingleThreadExecutor().submit(usbIoManager_card);
    }*/

    public void send_card(String s){
        try {
            byte bytes[] = Util.hexStr2Byte(s);
            //byte bytes[] = Util.toByteArray2(s);
            port_card.write(bytes, bytes.length);
            //Toast.makeText(this, "Succeed to send " + s, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(MainActivity.this, "发送失败!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void send_lora(byte[] bytes){
        try {
            //byte bytes[] = Util.toByteArray2(s);
            port_lora.write(bytes,bytes.length);
            // Toast.makeText(this, "Succeed to send " + s, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(MainActivity.this, "发送失败!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class UsbPermissionActionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        // user choose YES for your previously popup window asking for grant perssion for this usb device
                    } else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    //按指定的数据格式传送信息至Lora（参数一为目标地址，参数二为信道）
    public  void send_polling(byte address,byte channel){
        int p=0;
        byte []tempData=new byte[2];
        byte []Data=new byte[6];
        Data[p++]=0;
        Data[p++]=address;
        Data[p++]=channel;
        Data[p++]='(';
        Data[p++]=0;
        tempData[0]=Data[p-1];
        Data[p++]=2;
        tempData[1]=Data[p-1];
        Data=byteMerger(Data,Util.hexStr2Byte(getCRC(tempData)));
        Data=byteMerger(Data,Util.toByteArray2(")"));
        send_lora(Data);
    }

    //CRC校验位（返回String）
    public static String getCRC(byte[] data) {
        byte[] crc16_h = {
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40
        };

        byte[] crc16_l = {
                (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04,
                (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8,
                (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,
                (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4,
                (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38,
                (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C,
                (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7, (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,
                (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4,
                (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68,
                (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C,
                (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0,
                (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,
                (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98,
                (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D, (byte) 0x4C, (byte) 0x8C,
                (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40
        };
        int crc = 0x0000ffff;
        int ucCRCHi = 0x00ff;
        int ucCRCLo = 0x00ff;
        int iIndex;
        for (int i = 0; i < data.length; ++i) {
            iIndex = (ucCRCLo ^ data[i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_h[iIndex];
            ucCRCHi = crc16_l[iIndex];
        }
        crc = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        //高低位互换，输出符合相关工具对Modbus CRC16的运算
        crc = ( (crc & 0xFF00) >> 8) | ( (crc & 0x00FF ) << 8);
        return String.format("%04X", crc);

    }

    //将字符串转为Byte类型
    public static byte strToByte(String temp) {
        byte byt=Byte.valueOf(temp);
        return byt;
    }
    //两个Byte数组合并
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}

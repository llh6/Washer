package com.example.wash;


import static com.example.wash.ui.fragment_Wash.getAllWashers;
import static com.example.wash.ui.fragment_Wash.washerslist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wash.entity.DataAll;
import com.example.wash.entity.Utility;
import com.example.wash.entity.Wash;
import com.example.wash.self_style.WheelView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_Setting extends AppCompatActivity {

    private String selectText = "";
    private ArrayList<String> numberlist1 = new ArrayList<>();
    private ArrayList<String> numberlist2 = new ArrayList<>();
    private ArrayList<String> numberlist3 = new ArrayList<>();
    public static TextView numLimitation1 ;
    public static TextView numLimitation2 ;
    public static TextView numLimitation3 ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button confirm = findViewById(R.id.confirm);
        Button cancel = findViewById(R.id.cancel);
        numLimitation1 = findViewById(R.id.num_limitation1);
        numLimitation2 = findViewById(R.id.num_limitation2);
        numLimitation3 = findViewById(R.id.num_limitation3);
        numLimitation1.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});
        numLimitation2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
        initData();
        numLimitation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation1, numberlist1, 0);
            }
        });
        numLimitation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation2, numberlist2, 0);
            }
        });
        numLimitation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(numLimitation3, numberlist3, 0);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Setting.this,MainActivity.class);
                int start_num = Integer.parseInt(numLimitation1.getText().toString());
                int end_num = Integer.parseInt(numLimitation2.getText().toString());
                String address = numLimitation3.getText().toString();
                if (address.equals("南区宿舍")){
                    Toast.makeText(Activity_Setting.this, "南区宿舍还未建造完毕哦~", Toast.LENGTH_SHORT).show();
                }else {
                    ProgressDialog pd2 = new ProgressDialog(Activity_Setting.this);
                    pd2.setTitle("提示");
                    pd2.setMessage("玩命加载中...");
                    pd2.setCancelable(true);
                    pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd2.show();
                    getRangeWashers(start_num,end_num,address);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                            pd2.cancel();
                            finish();
                        }
                    },1000);
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Setting.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                finish();
            }
        });
    }

    //数字选择器
    private void initData() {
        numberlist1.clear();
        numberlist2.clear();
        for (int i = 1; i <= 99; i++) {
            numberlist1.add(String.format("%d", i));
        }
        for (int i = 1; i <= 100; i++) {
            numberlist2.add(String.format("%d", i));
        }
        for (int i = 1; i <= 4 ; i++) {
            if (i==1){
                numberlist3.add("全部");
            }
            if (i==2){
                numberlist3.add("17A");
            }
            if(i==3){
                numberlist3.add("17B");
            }
            if(i==4){
                numberlist3.add("南区宿舍");
            }
        }
    }


    private void showDialog(TextView textView, ArrayList<String> list, int selected){
        showChoiceDialog(list, textView, selected,
                new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        selectText = item;
                    }
                });
    }

    private void showChoiceDialog(ArrayList<String> dataList,final TextView textView,int selected,
                                  WheelView.OnWheelViewListener listener){
        selectText = "";
        View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview,null);
        final WheelView wheelView = outerView.findViewById(R.id.wheel_view);
        wheelView.setOffset(2);// 对话框中当前项上面和下面的项数
        wheelView.setItems(dataList);// 设置数据源
        wheelView.setSeletion(selected);// 默认选中第三项
        wheelView.setOnWheelViewListener(listener);

        // 显示对话框，点击确认后将所选项的值显示到EditText上
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确认",
                        (dialogInterface, i) -> {
                            textView.setText(selectText);
                            textView.setTextColor(this.getResources().getColor(R.color.black));
                        })
                .setNegativeButton("取消",null).create();
        alertDialog.show();
        int green = this.getResources().getColor(R.color.black);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(green);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(green);
    }

    public class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))

                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
    public static void getRangeWashers(int start_num, int end_num, String address) {
        washerslist.clear();
        String start = null;
        String end = null;
        Format f1 = new DecimalFormat("000");
        if(address.equals("全部")){
            getAllWashers();
            return;
        }
        else if (address.equals("17A")){
            start = String.valueOf(1701)+f1.format(start_num);
            end = String.valueOf(1701)+f1.format(end_num);
        }else if (address.equals("17B")){
            start = String.valueOf(1702)+f1.format(start_num);
            end = String.valueOf(1702)+f1.format(end_num);
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.161.128.250/api/washer?pageNum=1&pageSize=10&search=&widStart="+start+"&widEnd="+end)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fail", "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                DataAll dataAll = Utility.handleWashersResponse(responseText);
                for (Wash wash : dataAll.washerList) {
                    washerslist.add(wash);
                }
            }
        });
    }
}

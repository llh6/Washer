package com.example.wash.entity;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wash {
    @SerializedName("area")
    private String address;
    private String channel;
    @SerializedName("wid")
    private String wid;
    private String num ;
    @SerializedName("status")
    private String status;
    @SerializedName("endTime")
    private String time;
    private String money="";
//    public Wash(String status,String number){
//        this.status=status;
//        this.num=number;
//    }
//    public Wash(String address, String channel, String num, String status, String time, String money) {
//        this.address = address;
//        this.channel = channel;
//        this.num = num;
//        this.status = status;
//        this.time = time;
//        this.money = money;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNum() {
        String temp = wid.substring(0,4);
        if (temp.equals("1701")){
            this.num="17A"+wid.substring(4,7);
        }
        else if(temp.equals("1702")){
            this.num = "17B"+wid.substring(4,7);
        }else{
            this.num = wid;
        }
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getstatus() {
        if(status.equals("Y")) {
            return "空闲";
        }else if (status.equals("Norm")||status.equals("Spin")){
            return "忙碌";
        }else {
            return "故障";
        }
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getRemainTime() {
        String remian_time = "";
        if (status.equals("Norm")||status.equals("Spin")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            try {
                Date date_end  = simpleDateFormat.parse(time);
                Date date_now = new Date(System.currentTimeMillis());
                long res = date_end.getTime()-date_now.getTime();
                res = res/1000/60;
                remian_time = String.valueOf(res);
                if (res<=0){
                    status="Y";
                }
            }catch (Exception e){

            }
        }
        return remian_time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWid() {
        return wid;
    }

    public int matchNum(String str){
        String regEx="[^0-9]";
        Pattern p=Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim()) ;
    }
}

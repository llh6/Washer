package com.example.wash.entity;

import com.google.gson.annotations.SerializedName;

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
    private String time;
    private String money;
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
            this.num = "测试用例";
        }
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getstatus() {
        if(status.equals("Y")) {
            return "空闲";
        }else if (status.equals("N")){
            return "忙碌";
        }else {
            return "故障";
        }
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
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

    public int matchNum(String str){
        String regEx="[^0-9]";
        Pattern p=Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim()) ;
    }
}

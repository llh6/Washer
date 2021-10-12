package com.example.wash.Service;

import static android.content.ContentValues.TAG;
import static com.example.wash.Activity_Setting.address;
import static com.example.wash.Activity_Setting.end_num;
import static com.example.wash.Activity_Setting.getRangeWashers;
import static com.example.wash.Activity_Setting.start_num;
import static com.example.wash.ui.fragment_Wash.getAllWashers;
import static com.example.wash.ui.fragment_Wash.myRecyclerAdapter;
import static com.example.wash.ui.fragment_Wash.washerslist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.wash.entity.DataAll;
import com.example.wash.entity.Utility;
import com.example.wash.entity.Wash;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AutoUpdateService extends Service {
    public List<Wash> washerslist_temp =new ArrayList<Wash>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //update_item();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+time;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        myRecyclerAdapter.notifyDataSetChanged();
        return super.onStartCommand(intent, flags, startId);
    }

    private void update_item() {
        if(washerslist.size()==0&&start_num==0&&end_num==0)
        {
            Log.i(TAG, "update: 666");
            getAllWashers();
        }else if (washerslist.size()!=0&&start_num==0&&end_num==0){
            Log.i(TAG, "update: 777");
            getAllWashers();
        }else if (washerslist.size()!=0&&start_num!=0&&end_num!=0)
        {
            Log.i(TAG, "update: 888");
            getRangeWashers(start_num,end_num,address);
        }
        myRecyclerAdapter.notifyDataSetChanged();
    }

    private void request(int start_num, int end_num, String address) {
        String start = null;
        String end = null;
        Format f1 = new DecimalFormat("000");
        if(address.equals("全部")){
            washerslist_temp.clear();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.161.128.250:9090/washer")
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
                        washerslist_temp.add(wash);
                    }
                }
            });
            return;
        }
        else if (address.equals("17A")){
            washerslist_temp.clear();
            start = String.valueOf(1701)+f1.format(start_num);
            end = String.valueOf(1701)+f1.format(end_num);
        }else if (address.equals("17B")){
            washerslist_temp.clear();
            start = String.valueOf(1702)+f1.format(start_num);
            end = String.valueOf(1702)+f1.format(end_num);
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.161.128.250/api/washer?pageNum=1&pageSize=10&search=&widStart="+start+"&widEnd="+end+"&widTarget=&widStatus=")
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
                    washerslist_temp.add(wash);
                }
            }
        });
    }
}

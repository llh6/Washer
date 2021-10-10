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


public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //update();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+time;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
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
}

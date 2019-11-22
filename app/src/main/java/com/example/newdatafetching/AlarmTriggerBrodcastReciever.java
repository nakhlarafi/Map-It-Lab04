package com.example.newdatafetching;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

public class AlarmTriggerBrodcastReciever extends CountDownTimer {
    private final static String TAG = "ALARM_TRIGGER_BROADCAST";
    Context context;

    public AlarmTriggerBrodcastReciever(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }


    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {
        System.out.println("***********************");
        Toast.makeText(context,"Stopeed!",Toast.LENGTH_SHORT).show();
    }
}

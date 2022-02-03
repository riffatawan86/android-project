package com.test.project.samplelocation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Triggered!", Toast.LENGTH_SHORT).show();
        Log.d("ALARM-NEW", "Alarm Triggered!");
    }
}

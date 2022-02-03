package com.test.project.samplelocation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.test.project.samplelocation.interfaces.MyReceiverCallBack;
import com.test.project.samplelocation.models.MyLocationModel;
import com.test.project.samplelocation.utils.MyConstants;

public class AppBroadCastReceiver extends BroadcastReceiver {

    private MyReceiverCallBack callBack;

    public AppBroadCastReceiver(MyReceiverCallBack callBack) {
        this.callBack = callBack;
    }

    public AppBroadCastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(MyConstants.Companion.getMY_BROADCAST_ACTION())) {
                MyLocationModel model = intent.getParcelableExtra(MyConstants.Companion.getDATA_FROM_SERVICE_TO_RECEIVER());
                callBack.setLocationUpdates(model);
            }
        }
    }
}

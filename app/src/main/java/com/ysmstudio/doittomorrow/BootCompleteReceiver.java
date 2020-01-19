package com.ysmstudio.doittomorrow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        DoItTomorrow doItTomorrow = (DoItTomorrow) context.getApplicationContext();
        doItTomorrow.createAlarm();
    }
}

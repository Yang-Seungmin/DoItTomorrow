package com.ysmstudio.doittomorrow;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import io.realm.Realm;

public class DoItTomorrow extends Application {
    public static final int REQ_TODAY_TODO_PENDING = 100;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        createTodayTodoNotiPendingIntent();
    }

    private void createTodayTodoNotiPendingIntent() {
        Intent intent = new Intent("com.ysmstudio.doittomorrow.RESET_ALARM");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                REQ_TODAY_TODO_PENDING,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}

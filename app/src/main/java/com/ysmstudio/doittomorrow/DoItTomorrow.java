package com.ysmstudio.doittomorrow;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.Preference;

import java.util.Calendar;

import io.realm.Realm;

public class DoItTomorrow extends Application {
    public static final int REQ_TODAY_TODO_PENDING = 100;

    public static final String CHANNEL_ID_TODAY_TODO = "TODAY_TODO_CHANNEL";

    private SharedPreferences timePreference;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Realm.init(this);
        timePreference = getSharedPreferences("pref_time", MODE_PRIVATE);
        createTodayTodoNotiPendingIntent(
                timePreference.getInt("reset_hour", 6),
                timePreference.getInt("reset_minute", 0)
        );
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_TODAY_TODO,
                    "Today todo notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notify todo at today");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createTodayTodoNotiPendingIntent(int hour, int minute) {
        Intent intent = new Intent("com.ysmstudio.doittomorrow.RESET_ALARM");
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                REQ_TODAY_TODO_PENDING,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}

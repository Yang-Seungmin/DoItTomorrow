package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_TODO_INSERT = "TODOINSERT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
    }

    //Oreo 이후 버전의 Notification Channel 추가
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_TODO_INSERT,
                    "Todo Insert Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }
    }
}

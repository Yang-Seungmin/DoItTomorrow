package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_TODO_INSERT = "TODO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Intent intent = new Intent(MainActivity.this, TodoService.class);
        startService(intent);
    }

    //Oreo 이후 버전의 Notification Channel 추가
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_TODO_INSERT,
                    "All todo Notification",
                    NotificationManager.IMPORTANCE_MIN
            );

            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }
    }
}

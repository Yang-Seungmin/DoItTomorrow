package com.ysmstudio.doittomorrow;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TodoService extends Service {
    public static final int SERVICE_ID = 1001;

    public TodoService() {
    }

    public class TodoInputServiceBinder extends Binder {
        TodoService getService() {
            return TodoService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(SERVICE_ID, createNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        Notification notification = new NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_TODO_INSERT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("오늘은 4개의 할 일이 있습니다")
                .setContentText("aaa")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();

        return notification;
    }
}

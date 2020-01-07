package com.ysmstudio.doittomorrow;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class TodoInputService extends Service {
    public static final int SERVICE_ID = 1001;

    private final IBinder binder = new TodoInputServiceBinder();

    public TodoInputService() {
    }

    public class TodoInputServiceBinder extends Binder {
        TodoInputService getService() {
            return TodoInputService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(SERVICE_ID, createNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification() {
        Notification notification = new NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_TODO_INSERT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();

        return notification;
    }
}

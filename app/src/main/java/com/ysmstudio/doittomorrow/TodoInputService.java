package com.ysmstudio.doittomorrow;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class TodoInputService extends Service {

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

    private void createNotification() {
        Notification notification = new NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_TODO_INSERT)
                .build();
    }
}

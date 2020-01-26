package com.ysmstudio.doittomorrow;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.preference.Preference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;

/**
 * 전역 Application 클래스
 */
public class DoItTomorrow extends Application {
    public static final int REQ_TODAY_TODO_PENDING = 100;

    public static final String CHANNEL_ID_TODAY_TODO = "TODAY_TODO_CHANNEL";

    private SharedPreferences timePreference;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Realm.init(this);
        createAlarm();
    }

    /**
     * 매일 알림을 위해 AlarmService를 통해 알람을 생성한다.
     * 이미 있을 경우 그 알람을 제거하고 새로 만든다.
     */
    public void createAlarm() {
        timePreference = getSharedPreferences("pref_time", MODE_PRIVATE);
        pendingIntent = createTodayTodoNotiPendingIntent(
                timePreference.getInt("reset_hour", 6),
                timePreference.getInt("reset_minute", 0)
        );
        removeAlarm();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
        calendar.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));
        calendar.set(Calendar.SECOND, 0);

        if(BuildConfig.DEBUG)
            Toast.makeText(this, SimpleDateFormat.getDateTimeInstance().format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
    }

    /**
     * 이미 알람이 set되어 있을 경우 알람을 지운다.
     */
    public void removeAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }

    /**
     * Android 8.0 이상에서 작동하는 알림 채널을 생성하는 함수
     */
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

    /**
     * 알람에 반응할 Receiver를 PendingIntent를 통해 등록한다.
     * @param hour
     * @param minute
     * @return
     */
    public PendingIntent createTodayTodoNotiPendingIntent(int hour, int minute) {
        Intent intent = new Intent(this, TodayTodoNotiReceiver.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                REQ_TODAY_TODO_PENDING,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        return pendingIntent;
    }
}

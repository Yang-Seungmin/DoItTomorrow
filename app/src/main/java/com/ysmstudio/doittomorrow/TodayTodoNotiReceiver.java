package com.ysmstudio.doittomorrow;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class TodayTodoNotiReceiver extends BroadcastReceiver {

    public static final int NOTI_ID_TODAY_TODO = 101;

    private Context context;
    private int hour, minute;

    private SharedPreferences timePreference;

    private RealmResults<TodoData> todoDataRealmResults;
    private RealmChangeListener<RealmResults<TodoData>> todoDataRealmChangeListener = new RealmChangeListener<RealmResults<TodoData>>() {
        @Override
        public void onChange(RealmResults<TodoData> todoData) {
            createNotification(context);
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        this.context = context;
        hour = intent.getExtras().getInt("hour");
        minute = intent.getExtras().getInt("minute");

        loadTodoData();
    }

    private void createNotification(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < todoDataRealmResults.size(); i++) {
            stringBuilder.append(todoDataRealmResults.get(i));
            if(i < todoDataRealmResults.size() - 1) stringBuilder.append('\n');
        }

        Notification notification = new NotificationCompat.Builder(context, DoItTomorrow.CHANNEL_ID_TODAY_TODO)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getQuantityString(R.plurals.noti_today_todo_title,
                        todoDataRealmResults.size(),
                        todoDataRealmResults.size()))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(stringBuilder))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTI_ID_TODAY_TODO, notification);
    }

    private void loadTodoData() {
        RealmConfiguration todoRealmConfiguration = new RealmConfiguration.Builder()
                .name("todos.realm").build();

        Realm todoRealm = Realm.getInstance(todoRealmConfiguration);
        long[] times = getListTimeMilis();
        todoDataRealmResults = todoRealm.where(TodoData.class)
                .greaterThan("createdDate", times[0])
                .lessThan("createdDate", times[1])
                .findAllAsync();
        todoDataRealmResults.addChangeListener(todoDataRealmChangeListener);
    }

    private long[] getListTimeMilis() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, hour);
        calendarStart.set(Calendar.MINUTE, minute);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        calendarStart.add(Calendar.DATE, -1);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, hour);
        calendarEnd.set(Calendar.MINUTE, minute);
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

        Log.d("times", "From " + dateFormat.format(calendarStart.getTimeInMillis()) + " End " + dateFormat.format(calendarEnd.getTimeInMillis()));

        return new long[]{calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis()};
    }
}

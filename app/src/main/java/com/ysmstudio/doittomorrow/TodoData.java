package com.ysmstudio.doittomorrow;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TodoData extends RealmObject {
    private String name;

    @PrimaryKey
    private long createdDate;

    private boolean isChecked;

    public TodoData() {
        this.name = null;
        createdDate = System.currentTimeMillis();
        isChecked = false;
    }

    public TodoData(String name) {
        this.name = name;
        createdDate = System.currentTimeMillis();
        isChecked = false;
    }

    public TodoData(String name, long createdDate, boolean checked) {
        this.name = name;
        this.createdDate = createdDate;
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCreatedDateString() {
        if (BuildConfig.DEBUG) {
            Log.d("times_item", SimpleDateFormat.getDateTimeInstance().format(new Date(createdDate)));
            return String.valueOf(createdDate) + " | " + SimpleDateFormat.getDateTimeInstance().format(createdDate);
        }
        return SimpleDateFormat.getDateTimeInstance().format(createdDate);
    }

    @Override
    public String toString() {
        return name;
    }
}

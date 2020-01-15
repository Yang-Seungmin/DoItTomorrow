package com.ysmstudio.doittomorrow;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoData {

    String name;
    long createdDate;
    boolean checked;

    public TodoData(String name) {
        this.name = name;
        createdDate = System.currentTimeMillis();
        checked = false;
    }

    public TodoData(String name, long createdDate, boolean checked) {
        this.name = name;
        this.createdDate = createdDate;
        this.checked = checked;
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
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCreatedDateString() {
        return new SimpleDateFormat("a hh:mm").format(new Date(createdDate)) + "에 생성됨";
    }

    public int getVisibility() {
        return checked ? View.VISIBLE : View.INVISIBLE;
    }
}

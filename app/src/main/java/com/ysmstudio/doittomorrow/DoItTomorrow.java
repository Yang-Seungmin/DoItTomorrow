package com.ysmstudio.doittomorrow;

import android.app.Application;

import io.realm.Realm;

public class DoItTomorrow extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

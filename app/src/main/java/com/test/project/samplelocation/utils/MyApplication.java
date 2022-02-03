package com.test.project.samplelocation.utils;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
    public static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static String getStr(int id) {
        return myApplication.getString(id);
    }
}

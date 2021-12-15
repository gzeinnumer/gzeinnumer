package com.taxi.template.data;

import android.app.Application;

public class GlobalVariable extends Application {

    private static GlobalVariable mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GlobalVariable getInstance() {
        return mInstance;
    }

}

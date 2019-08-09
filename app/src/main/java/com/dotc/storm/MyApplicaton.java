package com.dotc.storm;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.dotc.ll.LocalLog;

import java.util.HashMap;
import java.util.Map;

import mobi.android.VideoXSDK;

public class MyApplicaton extends Application {
    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

}

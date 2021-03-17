package com.luding.smartoa.common;

import android.annotation.SuppressLint;
import androidx.multidex.MultiDexApplication;


/**
 * Created by lizhen on 2021/3/17.
 */
public class App extends MultiDexApplication {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}

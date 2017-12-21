package com.wind.advertisementreceiveapplication;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class AdvertisementReceiveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}

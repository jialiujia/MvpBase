package com.mobile.n900module.imp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mobile.n900module.device.N900Device;

public class N900Impl {
    private N900Device n900Device;
    private static Context context;
    private static final String TAG="N900Device";

    private SharedPreferences sharedPreferences;

    public N900Impl(Context mcontext)
    {
        context = mcontext;
        n900Device = N900Device.getInstance(context);
    }

    /**
     * 连接设备
     */
    public void connectDevice() {
        try {
            if (!n900Device.isDeviceAlive()) {
                n900Device.connectDevice();
                Log.i(TAG, "N900设备连接成功");
            } else {
                Log.i(TAG, "N900设备已经连接");
            }
        } catch (Exception e) {
            Log.i(TAG, "N900设备连接异常");
        }
    }

    /**
     * 删除设备
     */
    public void deltDevice() {
        n900Device.disconnect();
    }


    public static void processingLock() {
        SharedPreferences setting = context.getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("PBOC_LOCK", true);
        editor.apply();
    }

    public static boolean processingisLocked() {
        boolean result = false;
        processingUnLock();
        SharedPreferences setting = context.getSharedPreferences("setting", 0);
        if (setting.getBoolean("PBOC_LOCK", true)) {
            result = true;
        }
        return result;
    }

    public static void processingUnLock() {
        SharedPreferences setting = context.getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("PBOC_LOCK", false);
        editor.apply();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}

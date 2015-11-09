package com.socks.jiandan.utils;

import android.os.Build;
import android.os.StrictMode;

import com.socks.jiandan.BuildConfig;

/**
 * 开启严格模式，检测内存、硬盘等敏感操作
 * Created by zhaokaiqiang on 15/11/9.
 */
public class StrictModeUtil {

    public static void init() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            StrictMode.enableDefaults();
        }
    }

}

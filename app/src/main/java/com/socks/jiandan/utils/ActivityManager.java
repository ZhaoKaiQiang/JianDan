/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：AppManager.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-8-6
 */
package com.socks.jiandan.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @author zhaokaiqiang
 *         应用程序Activity管理类：用于Activity管理和应用程序退出
 * @date 2014-8-6 下午6:04:25
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {}

    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void finishAllActivityAndExit() {
        if (null != activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            System.exit(0);
        }
    }

}
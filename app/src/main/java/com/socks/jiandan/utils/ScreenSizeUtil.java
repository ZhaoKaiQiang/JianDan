package com.socks.jiandan.utils;

import android.app.Activity;

/**
 * Created by zhaokaiqiang on 15/4/9.
 */
public class ScreenSizeUtil {

	public static int getScreenWidth(Activity activity) {
		return activity.getWindowManager().getDefaultDisplay().getWidth();
	}

	public static int getScreenHeight(Activity activity) {
		return activity.getWindowManager().getDefaultDisplay().getHeight();
	}

}

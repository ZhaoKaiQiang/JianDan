package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class AppAplication extends Application {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		Fresco.initialize(mContext);
	}

	public static Context getContext() {
		return mContext;
	}

}
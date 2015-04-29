package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.socks.jiandan.utils.logger.Logger;

public class AppAplication extends Application {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		initImageLoader();
		Logger.init().hideThreadInfo();
	}

	public static Context getContext() {
		return mContext;
	}

	// 初始化ImageLoader
	public static void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}

}
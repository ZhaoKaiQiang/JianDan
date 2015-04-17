package com.socks.jiandan.utils;

import android.app.Activity;
import android.content.Intent;

import com.socks.jiandan.R;

/**
 * Created by zhaokaiqiang on 15/4/17.
 */
public class ShareUtil {

	public static void shareText(Activity activity, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				shareText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
				.string
				.app_name)));
	}

}

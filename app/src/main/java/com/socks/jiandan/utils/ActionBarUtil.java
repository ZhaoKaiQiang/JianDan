package com.socks.jiandan.utils;

import android.app.Activity;
import android.view.View;
import android.view.Window;

/**
 * Created by storm on 14-4-14.
 */
public class ActionBarUtil {

	public static View getActionBarView(Activity activity) {
		Window window = activity.getWindow();
		View v = window.getDecorView();
//		int resId = activity.getResources().getIdentifier("action_bar_container", "id", "android");
		int resId = activity.getResources().getIdentifier("action_bar_container", "id",
				activity.getPackageName());
		return v.findViewById(resId);
	}

	public static View findActionBarContainer(Activity activity) {
		int id = activity.getResources().getIdentifier("action_bar_container", "id",activity.getPackageName());
		return activity.findViewById(id);
	}

	public static View findSplitActionBar(Activity activity) {
		int id = activity.getResources().getIdentifier("split_action_bar", "id", "android");
		return activity.findViewById(id);
	}
}

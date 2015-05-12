package com.socks.jiandan.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.socks.jiandan.base.AppAplication;


/**
 * Created by storm on 14-3-25.
 */
public class RequestManager {

	public static RequestQueue mRequestQueue = Volley.newRequestQueue(AppAplication.getContext());

	private RequestManager() {
	}

	public static void addRequest(Request<?> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		mRequestQueue.add(request);

//		Logger.d("addRequest = " + request.getUrl());

	}

	public static void cancelAll(Object tag) {
		mRequestQueue.cancelAll(tag);
	}
}

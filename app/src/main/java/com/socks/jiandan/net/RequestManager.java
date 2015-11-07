package com.socks.jiandan.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.utils.logger.Logger;

public class RequestManager {

    public static RequestQueue mRequestQueue = Volley.newRequestQueue(JDApplication.getContext());

    private RequestManager() {
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
        Logger.d("Request = " + request.getUrl());
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}

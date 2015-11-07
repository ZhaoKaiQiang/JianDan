package com.socks.jiandan.base;

import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.net.ResponseFactory;


public class BaseFragment extends Fragment implements ConstantString {

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return ResponseFactory.getErrorListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}

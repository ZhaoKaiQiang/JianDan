package com.socks.jiandan.base;

import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.socks.jiandan.net.RequestManager;


public class BaseFragment extends Fragment implements ConstantString {

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}

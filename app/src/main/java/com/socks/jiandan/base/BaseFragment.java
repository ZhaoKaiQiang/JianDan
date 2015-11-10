package com.socks.jiandan.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.socks.jiandan.net.RequestManager;


public class BaseFragment extends Fragment implements ConstantString {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JDApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }
}

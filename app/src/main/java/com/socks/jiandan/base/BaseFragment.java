package com.socks.jiandan.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.net.ResponseFactory;


public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        mActionBar = ((AppCompatActivity) activity).getSupportActionBar();
//
//        if (activity instanceof MainActivity) {
//            mActionBar.getCustomView()
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            onActionBarClick();
//                        }
//                    });
//        }

    }

    /**
     * 重写该方法，可以自由的处理在MainActivity下的ActionBar的点击事件
     */
//    protected void onActionBarClick() {
//    }

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

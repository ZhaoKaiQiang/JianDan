package com.socks.jiandan.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.utils.ShowToast;

/**
 * Created by zhaokaiqiang on 15/11/6.
 */
public class ResponseFactory {

    public static Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowToast.Short(error.getMessage());
            }
        };
    }

}

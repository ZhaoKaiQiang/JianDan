package com.socks.jiandan.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 发表评论
 */
public class Request4PushComment extends Request<Boolean> {

    private Response.Listener<Boolean> listener;
    private HashMap<String, String> params;

    public Request4PushComment(String url, HashMap<String, String> params, Response
            .Listener<Boolean> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.params = params;
    }

    @Override
    protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {

        try {
            String resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            JSONObject resultObj = new JSONObject(resultStr);
            int code = resultObj.optInt("code");
            if (code == 0) {
                return Response.success(true, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyError("错误码:" + code));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }

    }

    /**
     * 包装请求参数
     *
     * @param thread_id
     * @return
     */
    public static HashMap<String, String> getRequestParams(String thread_id, String parent_id,
                                                           String author_name, String author_email,
                                                           String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("thread_id", thread_id);
        params.put("parent_id", parent_id);
        params.put("author_name", author_name);
        params.put("author_email", author_email);
        params.put("message", message);

        return params;
    }

    /**
     * 包装无父评论的请求参数
     *
     * @param thread_id
     * @param author_name
     * @param author_email
     * @param message
     * @return
     */
    public static HashMap<String, String> getRequestParamsNoParent(String thread_id,
                                                                   String author_name, String author_email,
                                                                   String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("thread_id", thread_id);
        params.put("author_name", author_name);
        params.put("author_email", author_email);
        params.put("message", message);

        return params;
    }


    @Override
    protected void deliverResponse(Boolean response) {
        listener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}

package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.Vote;

import java.io.UnsupportedEncodingException;

/**
 * 进行投票
 */
public class Request4Vote extends Request<Vote> {

    private Response.Listener<Vote> listener;

    public Request4Vote(String url, Response.Listener<Vote> listener,
                        Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<Vote> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(Vote.getInstance(jsonStr), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Vote response) {
        listener.onResponse(response);
    }

}

package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.CommentNumber;

import org.json.JSONObject;

import java.util.ArrayList;

public class Request4CommentCounts extends Request<ArrayList<CommentNumber>> {

    private Response.Listener<ArrayList<CommentNumber>> listener;

    public Request4CommentCounts(String url, Response.Listener<ArrayList<CommentNumber>> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<CommentNumber>> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("response");
            String[] comment_IDs = getUrl().split("\\=")[1].split("\\,");
            ArrayList<CommentNumber> commentNumbers = new ArrayList<>();

            for (String comment_ID : comment_IDs) {

                if (!jsonObject.isNull(comment_ID)) {
                    CommentNumber commentNumber = new CommentNumber();
                    commentNumber.setComments(jsonObject.getJSONObject(comment_ID).getInt(CommentNumber.COMMENTS));
                    commentNumber.setThread_id(jsonObject.getJSONObject(comment_ID).getString(CommentNumber.THREAD_ID));
                    commentNumber.setThread_key(jsonObject.getJSONObject(comment_ID).getString(CommentNumber.THREAD_KEY));
                    commentNumbers.add(commentNumber);
                } else {
                    //可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
                    commentNumbers.add(new CommentNumber("0", "0", 0));
                }
            }

            return Response.success(commentNumbers, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<CommentNumber> response) {
        listener.onResponse(response);
    }

}
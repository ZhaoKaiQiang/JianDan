package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.FreshNews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 新鲜事
 * Created by zhaokaiqiang on 15/4/8.
 */
public class Request4FreshNews extends Request<ArrayList<FreshNews>> {

	private Response.Listener<ArrayList<FreshNews>> listener;

	public Request4FreshNews(String url, Response.Listener<ArrayList<FreshNews>> listener,
	                         Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = listener;
	}

	@Override
	protected Response<ArrayList<FreshNews>> parseNetworkResponse(NetworkResponse response) {

		try {
			String resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			JSONObject resultObj = new JSONObject(resultStr);
			JSONArray postsArray = resultObj.optJSONArray("posts");
			return Response.success(FreshNews.parse(postsArray), HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<FreshNews> response) {
		listener.onResponse(response);
	}

}

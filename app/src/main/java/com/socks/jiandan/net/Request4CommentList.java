package com.socks.jiandan.net;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.Commentator;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/4/10.
 */
public class Request4CommentList extends Request<ArrayList<Commentator>> {

	private Response.Listener<ArrayList<Commentator>> listener;

	public Request4CommentList(String url, Response.Listener<ArrayList<Commentator>> listener,
	                           Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = listener;
	}

	@Override
	protected Response<ArrayList<Commentator>> parseNetworkResponse(NetworkResponse response) {

		try {
			//获取到所有的数据
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			//解析出所有的thread_id，并去掉非法字符，便与解析
			JSONObject resultJson = new JSONObject(jsonStr);
			String allThreadId = resultJson.getString("response").replace("[", "").replace
					("]", "").replace("\"", "");
			String[] threadIds = allThreadId.split("\\,");

			if (TextUtils.isEmpty(threadIds[0])) {
				return Response.success(new ArrayList<Commentator>(), HttpHeaderParser
						.parseCacheHeaders(response));
			} else {

				//然后根据thread_id再去获得对应的评论和作者信息
				JSONObject parentPostsJson = resultJson.getJSONObject("parentPosts");

				ArrayList<Commentator> commentators = new ArrayList<>();

				for (String threadId : threadIds) {
					Commentator commentator = new Commentator();
					JSONObject threadObject = parentPostsJson.getJSONObject(threadId);
					commentator.setMessage(threadObject.getString("message"));
					commentator.setCreated_at(threadObject.getString("created_at"));
					JSONObject authorObject = threadObject.getJSONObject("author");
					commentator.setName(authorObject.getString("name"));
					commentator.setAvatar_url(authorObject.getString("avatar_url"));
					commentators.add(commentator);
				}
				//解析评论，打上TAG
				return Response.success(commentators, HttpHeaderParser.parseCacheHeaders(response));
			}


		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}

	}

	@Override
	protected void deliverResponse(ArrayList<Commentator> response) {
		listener.onResponse(response);
	}
}

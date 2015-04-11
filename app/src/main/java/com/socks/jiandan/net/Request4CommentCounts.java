package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.Comment;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 评论数量请求器
 * Created by zhaokaiqiang on 15/4/8.
 */
public class Request4CommentCounts extends Request<ArrayList<Comment>> {

	private Response.Listener<ArrayList<Comment>> listener;

	public Request4CommentCounts(String url, Response.Listener<ArrayList<Comment>> listener,
	                             Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = listener;
	}

	@Override
	protected Response<ArrayList<Comment>> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("response");
			String[] comment_IDs = getUrl().split("\\=")[1].split("\\,");
			ArrayList<Comment> comments = new ArrayList<>();

			for (String comment_ID : comment_IDs) {

				if (!jsonObject.isNull(comment_ID)) {
					Comment comment = new Comment();
					comment.setThread_id(jsonObject.getJSONObject(comment_ID).getString(Comment.THREAD_ID));
					comment.setThread_key(jsonObject.getJSONObject(comment_ID).getString(Comment.THREAD_KEY));
					comment.setComments(jsonObject.getJSONObject(comment_ID).getInt(Comment.COMMENTS));
					comments.add(comment);
				} else {
					//可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
					comments.add(new Comment("0", "0", 0));
				}
			}

			return Response.success(comments, HttpHeaderParser.parseCacheHeaders(response));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<Comment> response) {
		listener.onResponse(response);
	}

}

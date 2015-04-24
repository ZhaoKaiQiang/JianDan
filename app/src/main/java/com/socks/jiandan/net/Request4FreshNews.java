package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.socks.jiandan.model.Author;
import com.socks.jiandan.model.CustomFields;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.model.Tags;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 段子数据请求器
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

			ArrayList<FreshNews> freshNewses = new ArrayList<>();

			for (int i = 0; i < postsArray.length(); i++) {

				FreshNews freshNews = new FreshNews();
				JSONObject jsonObject = postsArray.getJSONObject(i);

				freshNews.setId(jsonObject.optString("id"));
				freshNews.setUrl(jsonObject.optString("url"));
				freshNews.setTitle(jsonObject.optString("title"));
				freshNews.setDate(jsonObject.optString("date"));
				freshNews.setComment_count(jsonObject.optString("comment_count"));
				freshNews.setAuthor(Author.parse(jsonObject.optJSONObject("author")));
				freshNews.setCustomFields(CustomFields.parse(jsonObject.optJSONObject("custom_fields")));
				freshNews.setTags(Tags.parse(jsonObject.optJSONArray("tags")));

				freshNewses.add(freshNews);

			}

			return Response.success(freshNewses, HttpHeaderParser.parseCacheHeaders(response));

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

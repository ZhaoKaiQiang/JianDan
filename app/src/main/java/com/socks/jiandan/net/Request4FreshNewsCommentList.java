package com.socks.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.model.Comment4FreshNews;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新鲜事评论
 * Created by zhaokaiqiang on 15/4/8.
 */
public class Request4FreshNewsCommentList extends Request<ArrayList<Comment4FreshNews>> {

	private Response.Listener<ArrayList<Comment4FreshNews>> listener;
	private LoadFinishCallBack callBack;

	public Request4FreshNewsCommentList(String url, Response.Listener<ArrayList<Comment4FreshNews>> listener,
	                                    Response.ErrorListener errorListener, LoadFinishCallBack callBack) {
		super(Method.GET, url, errorListener);
		this.listener = listener;
		this.callBack = callBack;
	}

	@Override
	protected Response<ArrayList<Comment4FreshNews>> parseNetworkResponse(NetworkResponse response) {

		try {
			String resultStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			JSONObject resultObj = new JSONObject(resultStr);

			String status = resultObj.optString("status");

			if (status.equals("ok")) {
				String commentsStr = resultObj.optJSONObject("post").optJSONArray("comments")
						.toString();

				ArrayList<Comment4FreshNews> comment4FreshNewses = (ArrayList<Comment4FreshNews>) JSONParser.toObject(commentsStr,
						new TypeToken<ArrayList<Comment4FreshNews>>() {
						}.getType());

				Pattern pattern = Pattern.compile("\\d{7}");

				for (Comment4FreshNews comment4FreshNews : comment4FreshNewses) {

					comment4FreshNews.setType(Comment4FreshNews.TYPE_NORMAL);
					comment4FreshNews.setFloorNum(1);
					comment4FreshNews.setTag(Comment4FreshNews.TAG_NORMAL);

					//有回复
					if (comment4FreshNews.getContent().contains("#comment-")) {

						Matcher matcher = pattern.matcher(comment4FreshNews.getContent());

						if (matcher.find()) {
							comment4FreshNews.setParent(matcher.group(0));
							String content = comment4FreshNews.getContent().split
									("</a>:")[1];
							comment4FreshNews.setContent(content);
						}

					}

					String content = comment4FreshNews.getContent();
					content = content.replace("</p>", "");
					content = content.replace("<p>", "");
					content = content.replace("<br />", "");
					comment4FreshNews.setContent(content);
				}

				return Response.success(comment4FreshNewses, HttpHeaderParser
						.parseCacheHeaders(response));
			} else {
				return Response.error(new ParseError(new Exception("request failed")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<Comment4FreshNews> response) {
		listener.onResponse(response);
	}

}

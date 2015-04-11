package com.socks.jiandan;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.net.Request4CommentList;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.logger.Logger;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
	public ApplicationTest() {
		super(Application.class);
	}


	public void testGetCommentators() throws Exception {

		String url = "http://jandan.duoshuo.com/api/threads/listPosts.json?thread_key=comment-2750904";
		executeRequest(new Request4CommentList(url, new Response
				.Listener<ArrayList<Commentator>>() {
			@Override
			public void onResponse(ArrayList<Commentator> response) {
				Logger.d(response.toString());
			}
		}, errorListener()));

	}


	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ShowToast.Short(error.getMessage());
			}
		};
	}

}
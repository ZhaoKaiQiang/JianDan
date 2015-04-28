package com.socks.jiandan;

import android.test.InstrumentationTestCase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.model.Comment4FreshNews;
import com.socks.jiandan.net.Request4FreshNewsCommentList;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.logger.Logger;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/4/27.
 */
public class TestClass extends InstrumentationTestCase{

	public TestClass(){

	}

	public void testGetCommentators() throws Exception {

		executeRequest(new Request4FreshNewsCommentList(Comment4FreshNews.getUrlComments("" + 61245), new Response
				.Listener<ArrayList<Comment4FreshNews>>() {
			@Override
			public void onResponse(ArrayList<Comment4FreshNews> response) {
				Logger.d(response.toString());

				assertEquals(1, 2);

			}
		}, errorListener()));

		assertEquals(1,2);

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

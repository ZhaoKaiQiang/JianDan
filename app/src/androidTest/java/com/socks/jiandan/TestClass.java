package com.socks.jiandan;

import android.test.InstrumentationTestCase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.utils.ShowToast;

/**
 * Created by zhaokaiqiang on 15/4/27.
 */
public class TestClass extends InstrumentationTestCase{

	public TestClass(){

	}

	public void testGetCommentators() throws Exception {

		JDApplication.getContext();

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

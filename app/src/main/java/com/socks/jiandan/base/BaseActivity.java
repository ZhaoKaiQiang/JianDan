/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：BaseActivity.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-8-6
 */
package com.socks.jiandan.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.utils.AppManager;
import com.socks.jiandan.utils.ShowToast;

/**
 * @author zhaokaiqiang
 * @date 2014-8-6 下午5:49:10
 */
public class BaseActivity extends AppCompatActivity implements Initialable {

	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
		RequestManager.cancelAll(this);
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
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

	/**
	 * 设置默认的退出效果
	 */
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.anim_none, R.anim.trans_center_2_right);
	}
}

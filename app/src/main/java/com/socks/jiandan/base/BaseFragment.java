package com.socks.jiandan.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.ui.MainActivity;
import com.socks.jiandan.utils.ShowToast;


/**
 * Created by storm on 14-3-25.
 */
public abstract class BaseFragment extends Fragment {

	protected ActionBar mActionBar;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		mActionBar = actionBarActivity.getSupportActionBar();

		if (activity instanceof MainActivity) {
			mActionBar.getCustomView()
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							onActionBarClick();
						}
					});
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	/**
	 * 重写该方法，可以自由的处理在MainActivity下的ActionBar的点击事件
	 */
	public void onActionBarClick() {
	}

	protected void executeRequest(Request request) {
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		RequestManager.cancelAll(this);
	}
}

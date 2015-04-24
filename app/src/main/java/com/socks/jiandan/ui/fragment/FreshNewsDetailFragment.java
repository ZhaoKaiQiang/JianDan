package com.socks.jiandan.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.utils.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FreshNewsDetailFragment extends BaseFragment {

	@InjectView(R.id.webView)
	WebView webView;

	public FreshNewsDetailFragment() {
	}

	public static FreshNewsDetailFragment getInstance(FreshNews freshNews) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("FreshNews", freshNews);
		FreshNewsDetailFragment fragment = new FreshNewsDetailFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fresh_news_detail, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FreshNews freshNews = (FreshNews) getArguments().getSerializable("FreshNews");
		Logger.d("freshNews = " + freshNews);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(freshNews.getUrl());
	}

	@Override
	public void onResume() {
		super.onResume();
		if (webView != null) {
			webView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webView != null) {
			webView.onPause();
		}
	}
}

package com.socks.jiandan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.net.Request4FreshNewsDetail;
import com.socks.jiandan.ui.CommentList4FreshNewsActivity;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.String2TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FreshNewsDetailFragment extends BaseFragment {

	@InjectView(R.id.webView)
	WebView webView;

	private FreshNews freshNews;

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

		setHasOptionsMenu(true);

		freshNews = (FreshNews) getArguments().getSerializable("FreshNews");

		webView.getSettings().setJavaScriptEnabled(true);
		executeRequest(new Request4FreshNewsDetail(FreshNews.getUrlFreshNewsDetail(freshNews.getId()),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						webView.loadDataWithBaseURL("", getHtml(freshNews, response), "text/html", "utf-8", "");
					}
				}, new
				Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}));

	}

	private static String getHtml(FreshNews freshNews, String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>");
		sb.append("<html dir=\"ltr\" lang=\"zh\">");
		sb.append("<head>");
		sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
		sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
		sb.append("</head>");
		sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
		sb.append("<div id=\"pagewrapper\">");
		sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
		sb.append("<div id=\"maincontent\">");
		sb.append("<div class=\"post\">");
		sb.append("<div class=\"posthit\">");
		sb.append("<div class=\"postinfo\">");
		sb.append("<h2 class=\"thetitle\">");
		sb.append("<a>");
		sb.append(freshNews.getTitle());
		sb.append("</a>");
		sb.append("</h2>");
		sb.append(freshNews.getAuthor().getName() + " @ " + String2TimeUtil
				.dateString2GoodExperienceFormat(freshNews.getDate()));
		sb.append("</div>");
		sb.append("<div class=\"entry\">");
		sb.append(content);
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
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


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fresh_news_detail, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_comment:
				Intent intent = new Intent(getActivity(), CommentList4FreshNewsActivity.class);
				intent.putExtra("id", freshNews.getId());
				startActivity(intent);
				return true;
			case R.id.action_share:
				ShareUtil.shareText(getActivity(), freshNews.getTitle() + " " + freshNews.getUrl());
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

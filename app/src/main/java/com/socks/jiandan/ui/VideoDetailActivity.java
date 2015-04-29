package com.socks.jiandan.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 视频播放详情页
 */
public class VideoDetailActivity extends BaseActivity implements View.OnClickListener {

	@InjectView(R.id.webview)
	WebView webview;
	@InjectView(R.id.progress)
	ProgressBar progress;

	@InjectView(R.id.imgBtn_back)
	ImageButton imgBtn_back;
	@InjectView(R.id.imgBtn_forward)
	ImageButton imgBtn_forward;
	@InjectView(R.id.imgBtn_control)
	ImageButton imgBtn_control;

	private String url;

	private boolean isLoadFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		ButterKnife.inject(this);
		initView();
		initData();
	}

	@Override
	public void initView() {

		final ActionBar actionBar = getSupportActionBar();

		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		imgBtn_back.setOnClickListener(this);
		imgBtn_forward.setOnClickListener(this);
		imgBtn_control.setOnClickListener(this);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(
				new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {

						if (newProgress == 100) {
							progress.setVisibility(View.GONE);
						} else {
							progress.setProgress(newProgress);
							progress.setVisibility(View.VISIBLE);
						}

						super.onProgressChanged(view, newProgress);
					}
				}

		);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				imgBtn_control.setImageResource(R.drawable.ic_action_refresh);
				isLoadFinish = true;
				actionBar.setTitle(view.getTitle());
			}
		});

	}

	@Override
	public void initData() {
		url = getIntent().getStringExtra("url");
		webview.loadUrl(url);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgBtn_back:
				if (webview.canGoBack()) {
					webview.goBack();
				}
				break;
			case R.id.imgBtn_forward:
				if (webview.canGoForward()) {
					webview.goForward();
				}
				break;
			case R.id.imgBtn_control:

				if (isLoadFinish) {
					webview.reload();
					isLoadFinish = false;
				} else {
					webview.stopLoading();
				}
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (webview != null) {
			webview.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (webview != null) {
			webview.onPause();
		}
	}
}

package com.socks.jiandan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.TextUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoDetailActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
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

        initView();
        initData();
    }

    @Override
    public void initView() {

        ButterKnife.inject(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.loading);
        mToolbar.setNavigationIcon(R.drawable.ic_actionbar_back);

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
                mToolbar.setTitle(view.getTitle());
            }
        });

    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra("url");
        webview.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_more:
                new MaterialDialog.Builder(this)
                        .items(R.array.video_more)
                        .backgroundColor(getResources().getColor(JDApplication.COLOR_OF_DIALOG))
                        .contentColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    //分享
                                    case 0:
                                        ShareUtil.shareText(VideoDetailActivity.this, mToolbar.getTitle() + " " + url);
                                        break;
                                    //复制
                                    case 1:
                                        TextUtil.copy(VideoDetailActivity.this, url);
                                        break;
                                    //浏览器打开
                                    case 2:
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                        break;
                                }
                            }
                        }).show();
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

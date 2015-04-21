package com.socks.jiandan.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.utils.ScreenSizeUtil;
import com.socks.jiandan.utils.ShowToast;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhaokaiqiang on 15/4/21.
 */
public class ImageDetailActivity extends BaseActivity implements View.OnClickListener {

	@InjectView(R.id.img_back)
	ImageButton img_back;
	@InjectView(R.id.img_share)
	ImageButton img_share;
	@InjectView(R.id.web_gif)
	WebView webView;
	@InjectView(R.id.img)
	PhotoView img;
	@InjectView(R.id.progress)
	ProgressBar progress;

	private String author;
	private String[] urls;
	private String id;
	private String threadKey;
	private boolean is_need_webview;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private File imgCacheFile;

	private String imgPath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		ButterKnife.inject(this);
		initView();
		initData();
	}

	@Override
	public void initView() {
		img_back.setOnClickListener(this);
		img_share.setOnClickListener(this);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		author = intent.getStringExtra("img_author");
		urls = intent.getStringArrayExtra("img_url");
		id = intent.getStringExtra("img_id");
		threadKey = intent.getStringExtra("thread_key");
		is_need_webview = intent.getBooleanExtra("is_need_webview", false);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();

		if (is_need_webview) {

			img.setVisibility(View.GONE);

			webView.getSettings().setJavaScriptEnabled(true);
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			webView.addJavascriptInterface(new JSObject(), "external");
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					webView.loadUrl(url);
					return true;
				}

			});

			webView.setWebChromeClient(new WebChromeClient());

			imageLoader.displayImage(urls[0], img, options, new SimpleImageLoadingListener() {

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

					progress.setVisibility(View.GONE);

					imgCacheFile = DiskCacheUtils.findInCache(urls[0], imageLoader.getDiskCache());
					if (imgCacheFile != null) {
						imgPath = "file://" + imgCacheFile.getAbsolutePath();
						showImgInWebView(imgPath);
					}
				}

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progress.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					progress.setVisibility(View.GONE);
				}
			});

		} else {

			imageLoader.loadImage(urls[0], options, new
					SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

							progress.setVisibility(View.GONE);

							if (loadedImage.getHeight() > ScreenSizeUtil
									.getScreenWidth(ImageDetailActivity.this)) {
								imgCacheFile = DiskCacheUtils.findInCache(urls[0], imageLoader.getDiskCache());
								if (imgCacheFile != null) {
									imgPath = "file://" + imgCacheFile.getAbsolutePath();
									img.setVisibility(View.GONE);
									showImgInWebView(imgPath);
								}
							} else {
								img.setImageBitmap(loadedImage);
							}

						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progress.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							progress.setVisibility(View.GONE);
						}
					});

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.img_share:
				ShowToast.Short("分享");
				break;
		}

	}

	/**
	 * 使用ImageView加载GIF图片
	 *
	 * @param s
	 */
	private void showImgInWebView(final String s) {
		if (webView != null) {
			webView.loadDataWithBaseURL("", "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title></title><style type=\"text/css\"> html,body{width:100%;height:100%;margin:0;padding:0;background-color:black;} *{ -webkit-tap-highlight-color: rgba(0, 0, 0, 0);}#box{ width:100%;height:100%; display:table; text-align:center; background-color:black;} body{-webkit-user-select: none;user-select: none;-khtml-user-select: none;}#box span{ display:table-cell; vertical-align:middle;} #box img{  width:100%;} </style> </head> <body> <div id=\"box\"><span><img src=\"img_url\" alt=\"\"></span></div> <script type=\"text/javascript\" >document.body.onclick=function(e){window.external.onClick();e.preventDefault(); };function load_img(){var url=document.getElementsByTagName(\"img\")[0];url=url.getAttribute(\"src\");var img=new Image();img.src=url;if(img.complete){\twindow.external.img_has_loaded();\treturn;};img.onload=function(){window.external.img_has_loaded();};img.onerror=function(){\twindow.external.img_loaded_error();};};load_img();</script></body> </html>".replace("img_url", s), "text/html", "utf-8", "");
		}
	}

	public class JSObject {

		@JavascriptInterface
		public void img_has_loaded() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
				}
			});
		}

		@JavascriptInterface
		public void img_loaded_error() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ShowToast.Short(ToastMsg.LOAD_FAILED);
				}
			});
		}

		@JavascriptInterface
		public void onClick() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
				}
			});
		}
	}

}

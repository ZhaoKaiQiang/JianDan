package com.socks.jiandan.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.utils.ShowToast;

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
	WebView web_gif;
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

		if (is_need_webview) {
			web_gif.setVisibility(View.VISIBLE);
			progress.setIndeterminate(true);
		} else {
			img.setVisibility(View.VISIBLE);
		}

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
			ShowToast.Short("暂不支持GIF");
		} else {
			imageLoader.displayImage(urls[0], img, options, new
					SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							progress.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					progress.setProgress((int) (current * 100f / total));
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
}

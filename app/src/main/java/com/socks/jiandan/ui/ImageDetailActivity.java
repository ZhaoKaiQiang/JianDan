package com.socks.jiandan.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.utils.FileUtil;
import com.socks.jiandan.utils.JDMediaScannerConnectionClient;
import com.socks.jiandan.utils.ScreenSizeUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailActivity extends BaseActivity implements View.OnClickListener, LoadFinishCallBack {

    @InjectView(R.id.web_gif)
    WebView webView;
    @InjectView(R.id.img)
    PhotoView img;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.ll_bottom_bar)
    LinearLayout ll_bottom_bar;
    @InjectView(R.id.rl_top_bar)
    RelativeLayout rl_top_bar;

    public static final int ANIMATION_DURATION = 400;

    private String[] img_urls;
    private String threadKey;
    private String imgPath;
    private boolean isNeedWebView;
    private boolean isBarShow = true;
    private boolean isImgHaveLoad = false;
    private File imgCacheFile;
    private MediaScannerConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initView();
        initData();
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
    }

    @OnClick({R.id.img_back, R.id.img_share, R.id.tv_unlike, R.id.tv_like, R.id.img_comment, R.id.img_download})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_share:
                ShareUtil.sharePicture(this, img_urls[0]);
                break;
            case R.id.tv_like:
                ShowToast.Short("别点了，这玩意不能用");
                break;
            case R.id.tv_unlike:
                ShowToast.Short("别点了，这玩意不能用");
                break;
            case R.id.img_comment:
                Intent intent = new Intent(this, CommentListActivity.class);
                intent.putExtra(DATA_THREAD_KEY, threadKey);
                startActivity(intent);
                break;
            case R.id.img_download:
                FileUtil.savePicture(this, img_urls[0], this);
                break;
        }
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        img_urls = intent.getStringArrayExtra(DATA_IMAGE_URL);
        threadKey = intent.getStringExtra(DATA_THREAD_KEY);
        isNeedWebView = intent.getBooleanExtra(DATA_IS_NEED_WEBVIEW, false);

        if (isNeedWebView) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(this, "external");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }

            });

            webView.setWebChromeClient(new WebChromeClient());
            webView.setBackgroundColor(Color.BLACK);

            img.setVisibility(View.GONE);
            ImageLoadProxy.displayImage4Detail(img_urls[0], img, new
                    SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progress.setVisibility(View.GONE);
                            imgCacheFile = DiskCacheUtils.findInCache(img_urls[0], ImageLoadProxy.getImageLoader().getDiskCache());
                            if (imgCacheFile != null) {
                                imgPath = "file://" + imgCacheFile.getAbsolutePath();
                                showImgInWebView(imgPath);
                                isImgHaveLoad = true;
                            }
                        }

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progress.setVisibility(View.GONE);
                            ShowToast.Short("加载失败" + failReason.getType().name());
                        }
                    });

        } else {
            ImageLoadProxy.loadImageFromLocalCache(img_urls[0], new
                    SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progress.setVisibility(View.GONE);
                            if (loadedImage.getHeight() > ScreenSizeUtil
                                    .getScreenWidth(ImageDetailActivity.this)) {
                                imgCacheFile = DiskCacheUtils.findInCache(img_urls[0], ImageLoadProxy.getImageLoader().getDiskCache());
                                if (imgCacheFile != null) {
                                    imgPath = "file://" + imgCacheFile.getAbsolutePath();
                                    img.setVisibility(View.GONE);
                                    showImgInWebView(imgPath);
                                    isImgHaveLoad = true;
                                }
                            } else {
                                img.setImageBitmap(loadedImage);
                                isImgHaveLoad = true;
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
        img.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                toggleBar();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        toggleBar();
    }

    private void toggleBar() {
        if (isImgHaveLoad) {
            //隐藏
            if (isBarShow) {
                isBarShow = false;
                ObjectAnimator
                        .ofFloat(ll_bottom_bar, "translationY", 0, ll_bottom_bar.getHeight())
                        .setDuration(ANIMATION_DURATION)
                        .start();
                ObjectAnimator
                        .ofFloat(rl_top_bar, "translationY", 0, -rl_top_bar.getHeight())
                        .setDuration(ANIMATION_DURATION)
                        .start();
            } else {
                //显示
                isBarShow = true;
                ObjectAnimator
                        .ofFloat(ll_bottom_bar, "translationY", ll_bottom_bar.getHeight(), 0)
                        .setDuration(ANIMATION_DURATION)
                        .start();
                ObjectAnimator
                        .ofFloat(rl_top_bar, "translationY", -rl_top_bar.getHeight(), 0)
                        .setDuration(ANIMATION_DURATION)
                        .start();
            }
        }
    }

    private void showImgInWebView(final String s) {
        if (webView != null) {
            webView.loadDataWithBaseURL("", "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title></title><style type=\"text/css\"> html,body{width:100%;height:100%;margin:0;padding:0;background-color:black;} *{ -webkit-tap-highlight-color: rgba(0, 0, 0, 0);}#box{ width:100%;height:100%; display:table; text-align:center; background-color:black;} body{-webkit-user-select: none;user-select: none;-khtml-user-select: none;}#box span{ display:table-cell; vertical-align:middle;} #box img{  width:100%;} </style> </head> <body> <div id=\"box\"><span><img src=\"img_url\" alt=\"\"></span></div> <script type=\"text/javascript\" >document.body.onclick=function(e){window.external.onClick();e.preventDefault(); };function load_img(){var url=document.getElementsByTagName(\"img\")[0];url=url.getAttribute(\"src\");var img=new Image();img.src=url;if(img.complete){\twindow.external.img_has_loaded();\treturn;};img.onload=function(){window.external.img_has_loaded();};img.onerror=function(){\twindow.external.img_loaded_error();};};load_img();</script></body> </html>".replace("img_url", s), "text/html", "utf-8", "");
        }
    }

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
                ShowToast.Short(ConstantString.LOAD_FAILED);
            }
        });
    }

    @JavascriptInterface
    public void onClick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleBar();
            }
        });
    }

    @Override
    public void loadFinish(Object obj) {
        //下载完图片后，通知更新
        Bundle bundle = (Bundle) obj;
        boolean isSmallPic = bundle.getBoolean(DATA_IS_SIAMLL_PIC);
        String filePath = bundle.getString(DATA_FILE_PATH);
        File newFile = new File(filePath);
        JDMediaScannerConnectionClient connectionClient = new JDMediaScannerConnectionClient(isSmallPic,
                newFile);
        connection = new MediaScannerConnection(this, connectionClient);
        connectionClient.setMediaScannerConnection(connection);
        connection.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }

        if (img.getVisibility() == View.VISIBLE) {
            ImageLoadProxy.getImageLoader().cancelDisplayTask(img);
        }
    }
}

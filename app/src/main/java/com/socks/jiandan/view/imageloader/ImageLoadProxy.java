package com.socks.jiandan.view.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zhaokaiqiang on 15/11/7.
 */
public class ImageLoadProxy {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void displayImage(String url, ImageView target) {
        imageLoader.displayImage(url, target);
    }

    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
        imageLoader.displayImage(url, target, options);
    }

    public static DisplayImageOptions getOptions4ResetView(int loadingResource) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .build();
    }

}

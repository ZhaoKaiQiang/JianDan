package com.socks.jiandan.view.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.socks.jiandan.R;

/**
 * Created by zhaokaiqiang on 15/11/7.
 */
public class ImageLoadProxy {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
        imageLoader.displayImage(url, target, options);
    }

    public static void displayHeadIcon(String url, ImageView target) {
        imageLoader.displayImage(url, target, getOptions4Header());

    }

    public static void displayImage4Detail(String url, ImageView target, SimpleImageLoadingListener loadingListener) {
        imageLoader.displayImage(url, target, getOption4Detail(), loadingListener);
    }

    public static void displayImageList(String url, ImageView target, int loadingResource, SimpleImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        imageLoader.displayImage(url, target, getOptions4ResetView(loadingResource), loadingListener, progressListener);
    }

    public static void loadImage(String url, SimpleImageLoadingListener loadingListener) {
        imageLoader.loadImage(url, getOption4Detail(), loadingListener);
    }

    public static DisplayImageOptions getOption4Detail() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }


    public static DisplayImageOptions getOptions4Header() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.ic_loading_small)
                .showImageOnFail(R.drawable.ic_loading_small)
                .showImageOnLoading(R.drawable.ic_loading_small)
                .build();
    }

    public static DisplayImageOptions getOptions4ResetView(int loadingResource) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(loadingResource)
                .showImageOnFail(loadingResource)
                .build();
    }

}

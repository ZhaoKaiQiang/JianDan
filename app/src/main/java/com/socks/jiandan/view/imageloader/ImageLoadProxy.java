package com.socks.jiandan.view.imageloader;

import android.widget.ImageView;

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


}

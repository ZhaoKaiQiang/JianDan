package com.socks.jiandan.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.R;
import com.socks.jiandan.base.ConstantString;

import java.io.File;

public class ShareUtil {

    public static void sharePicture(Activity activity, String url) {

        String[] urlSplits = url.split("\\.");

        File cacheFile = ImageLoader.getInstance().getDiskCache().get(url);

        //如果不存在，则使用缩略图进行分享
        if (!cacheFile.exists()) {
            String picUrl = url;
            picUrl = picUrl.replace("mw600", "small").replace("mw1200", "small").replace
                    ("large", "small");
            cacheFile = ImageLoader.getInstance().getDiskCache().get(picUrl);
        }

        File newFile = new File(CacheUtil.getSharePicName
                (cacheFile, urlSplits));

        if (FileUtil.copyTo(cacheFile, newFile)) {
            ShareUtil.sharePicture(activity, newFile.getAbsolutePath(),
                    "分享自煎蛋 " + url);
        } else {
            ShowToast.Short(ConstantString.LOAD_SHARE);
        }
    }


    public static void shareText(Activity activity, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
                .string.app_name)));
    }

    public static void sharePicture(Activity activity, String imgPath, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        File f = new File(imgPath);
        if (f != null && f.exists() && f.isFile()) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        } else {
            ShowToast.Short("分享图片不存在哦");
            return;
        }

        //GIF图片指明出处url，其他图片指向项目地址
        if (imgPath.endsWith(".gif")) {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
                .string.app_name)));
    }


}

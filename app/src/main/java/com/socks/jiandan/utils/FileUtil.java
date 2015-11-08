package com.socks.jiandan.utils;

import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.callback.LoadFinishCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {

    /**
     * 获取文件夹总大小
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return (double) file.length() / 1024 / 1024;
            }
        } else {
            return 0.0;
        }
    }

    /**
     * 保存图片
     *
     * @param activity
     * @param picUrl
     */
    public static void savePicture(Activity activity, String picUrl, LoadFinishCallBack loadFinishCallBack) {

        boolean isSmallPic = false;
        String[] urls = picUrl.split("\\.");
        File cacheFile = ImageLoader.getInstance().getDiskCache().get(picUrl);

        //如果是GIF格式，优先保存GIF动态图，如果不存在，则保存缩略图
        if (!cacheFile.exists()) {
            String cacheUrl = picUrl.replace("mw600", "small").replace("mw1200", "small")
                    .replace("large", "small");
            cacheFile = ImageLoader.getInstance().getDiskCache().get(cacheUrl);
            isSmallPic = true;
        }

        File picDir = new File(CacheUtil.getSaveDirPath());

        if (!picDir.exists()) {
            picDir.mkdir();
        }

        final File newFile = new File(picDir, CacheUtil.getSavePicName(cacheFile, urls));

        if (FileUtil.copyTo(cacheFile, newFile)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BaseActivity.DATA_IS_SIAMLL_PIC, isSmallPic);
            bundle.putString(BaseActivity.DATA_FILE_PATH, newFile.getAbsolutePath());
            loadFinishCallBack.loadFinish(bundle);
        } else {
            ShowToast.Short(ConstantString.SAVE_FAILED);
        }

    }

    /**
     * 复制文件
     *
     * @param src 源文件
     * @param dst 目标文件
     * @return
     */
    public static boolean copyTo(File src, File dst) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            fo = new FileOutputStream(dst);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {

                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }


}

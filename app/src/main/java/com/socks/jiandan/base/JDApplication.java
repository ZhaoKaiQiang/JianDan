package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.socks.greendao.DaoMaster;
import com.socks.greendao.DaoSession;
import com.socks.jiandan.BuildConfig;
import com.socks.jiandan.cache.BaseCache;
import com.socks.jiandan.utils.logger.Logger;

public class JDApplication extends Application {

    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader();
        Logger.init().hideThreadInfo();
    }

    @Nullable
    public static Context getContext() {
        return mContext;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder build = new ImageLoaderConfiguration.Builder(this);
        build.tasksProcessingOrder(QueueProcessingType.LIFO);
        if (BuildConfig.DEBUG) {
//            build.writeDebugLogs();
        }
        ImageLoader.getInstance().init(build.build());
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
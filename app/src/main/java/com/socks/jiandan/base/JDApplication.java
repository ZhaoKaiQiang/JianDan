package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;

import com.socks.greendao.DaoMaster;
import com.socks.greendao.DaoSession;
import com.socks.jiandan.R;
import com.socks.jiandan.cache.BaseCache;
import com.socks.jiandan.utils.StrictModeUtil;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

public class JDApplication extends Application {

    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static int COLOR_OF_DIALOG = R.color.primary;
    public static int COLOR_OF_DIALOG_CONTENT = Color.WHITE;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ImageLoadProxy.initImageLoader(this);
        StrictModeUtil.init();
    }

    @Nullable
    public static Context getContext() {
        return mContext;
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
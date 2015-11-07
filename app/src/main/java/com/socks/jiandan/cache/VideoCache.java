package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.VideoCacheDao;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.model.Video;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

public class VideoCache extends BaseCache {

    private static VideoCache instance;
    private static VideoCacheDao mVideoCacheDao;

    private VideoCache() {
    }

    public static VideoCache getInstance(Context context) {

        if (instance == null) {

            synchronized (VideoCache.class) {
                if (instance == null) {
                    instance = new VideoCache();
                }
            }

            mDaoSession = JDApplication.getDaoSession(context);
            mVideoCacheDao = mDaoSession.getVideoCacheDao();
        }
        return instance;
    }

    public void clearAllCache() {
        mVideoCacheDao.deleteAll();
    }

    @Override
    public ArrayList<Video> getCacheByPage(int page) {

        QueryBuilder<com.socks.greendao.VideoCache> query = mVideoCacheDao.queryBuilder().where(VideoCacheDao.Properties.Page.eq("" + page));
        if (query.list().size() > 0) {
            return (ArrayList<Video>) JSONParser.toObject(query.list().get(0).getResult(),
                    new TypeToken<ArrayList<Video>>() {
                    }.getType());
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void addResultCache(String result, int page) {
        com.socks.greendao.VideoCache jokeCache = new com.socks.greendao.VideoCache();
        jokeCache.setResult(result);
        jokeCache.setPage(page);
        jokeCache.setTime(System.currentTimeMillis());
        mVideoCacheDao.insert(jokeCache);
    }

}

package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.VideoCache;
import com.socks.greendao.VideoCacheDao;
import com.socks.jiandan.base.AppAplication;
import com.socks.jiandan.model.Video;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class VideoCacheUtil extends BaseCacheUtil {

	private static VideoCacheUtil instance;
	private static VideoCacheDao mVideoCacheDao;

	private VideoCacheUtil() {
	}

	public static VideoCacheUtil getInstance(Context context) {

		if (instance == null) {

			synchronized (VideoCacheUtil.class) {
				if (instance == null) {
					instance = new VideoCacheUtil();
				}
			}

			mDaoSession = AppAplication.getDaoSession(context);
			mVideoCacheDao = mDaoSession.getVideoCacheDao();
		}
		return instance;
	}

	/**
	 * 清楚全部缓存
	 */
	public void clearAllCache() {
		mVideoCacheDao.deleteAll();
	}

	/**
	 * 根据页码获取缓存数据
	 *
	 * @param page
	 * @return
	 */
	@Override
	public ArrayList<Video> getCacheByPage(int page) {

		QueryBuilder<VideoCache> query = mVideoCacheDao.queryBuilder().where(VideoCacheDao.Properties.Page.eq("" + page));
		if (query.list().size() > 0) {
			return (ArrayList<Video>) JSONParser.toObject(query.list().get(0).getResult(),
					new TypeToken<ArrayList<Video>>() {
					}.getType());
		} else {
			return new ArrayList<Video>();
		}

	}

	/**
	 * 添加Jokes缓存
	 *
	 * @param result
	 * @param page
	 */
	@Override
	public void addResultCache(String result, int page) {
		VideoCache jokeCache = new VideoCache();
		jokeCache.setResult(result);
		jokeCache.setPage(page);
		jokeCache.setTime(System.currentTimeMillis());
		mVideoCacheDao.insert(jokeCache);
	}

}

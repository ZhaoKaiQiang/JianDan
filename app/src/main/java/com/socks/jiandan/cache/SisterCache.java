package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.SisterCacheDao;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

public class SisterCache extends BaseCache {

	private static SisterCache instance;
	private static SisterCacheDao mPictureCacheDao;

	private SisterCache() {
	}

	public static SisterCache getInstance(Context context) {

		if (instance == null) {

			synchronized (SisterCache.class) {
				if (instance == null) {
					instance = new SisterCache();
				}
			}

			mDaoSession = JDApplication.getDaoSession(context);
			mPictureCacheDao = mDaoSession.getSisterCacheDao();
		}
		return instance;
	}

	public void clearAllCache() {
		mPictureCacheDao.deleteAll();
	}

	@Override
	public ArrayList<Picture> getCacheByPage(int page) {

		QueryBuilder<com.socks.greendao.SisterCache> query = mPictureCacheDao.queryBuilder().where(SisterCacheDao
				.Properties.Page.eq(Integer.toString(page)));

		if (query.list().size() > 0) {
			return (ArrayList<Picture>) JSONParser.toObject(query.list().get(0).getResult(),
					new TypeToken<ArrayList<Picture>>() {
					}.getType());
		} else {
			return new ArrayList<>();
		}

	}

	@Override
	public void addResultCache(String result, int page) {
		com.socks.greendao.SisterCache pictureCache = new com.socks.greendao.SisterCache();
		pictureCache.setResult(result);
		pictureCache.setPage(page);
		pictureCache.setTime(System.currentTimeMillis());

		mPictureCacheDao.insert(pictureCache);
	}

}

package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.PictureCacheDao;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class PictureCache extends BaseCache {

	private static PictureCache instance;
	private static PictureCacheDao mPictureCacheDao;

	private PictureCache() {
	}

	public static PictureCache getInstance(Context context) {

		if (instance == null) {

			synchronized (PictureCache.class) {
				if (instance == null) {
					instance = new PictureCache();
				}
			}

			mDaoSession = JDApplication.getDaoSession(context);
			mPictureCacheDao = mDaoSession.getPictureCacheDao();
		}
		return instance;
	}

	/**
	 * 清楚全部缓存
	 */
	public void clearAllCache() {
		mPictureCacheDao.deleteAll();
	}

	/**
	 * 根据页码获取缓存数据
	 *
	 * @param page
	 * @return
	 */
	@Override
	public ArrayList<Picture> getCacheByPage(int page) {

		QueryBuilder<com.socks.greendao.PictureCache> query = mPictureCacheDao.queryBuilder().where(PictureCacheDao
				.Properties.Page.eq("" + page));

		if (query.list().size() > 0) {
			return (ArrayList<Picture>) JSONParser.toObject(query.list().get(0).getResult(),
					new TypeToken<ArrayList<Picture>>() {
					}.getType());
		} else {
			return new ArrayList<Picture>();
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
		com.socks.greendao.PictureCache pictureCache = new com.socks.greendao.PictureCache();
		pictureCache.setResult(result);
		pictureCache.setPage(page);
		pictureCache.setTime(System.currentTimeMillis());

		mPictureCacheDao.insert(pictureCache);
	}

}

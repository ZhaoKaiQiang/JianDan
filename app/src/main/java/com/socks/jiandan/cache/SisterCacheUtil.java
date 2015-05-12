package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.SisterCache;
import com.socks.greendao.SisterCacheDao;
import com.socks.jiandan.base.AppAplication;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class SisterCacheUtil extends BaseCacheUtil {

	private static SisterCacheUtil instance;
	private static SisterCacheDao mPictureCacheDao;

	private SisterCacheUtil() {
	}

	public static SisterCacheUtil getInstance(Context context) {

		if (instance == null) {

			synchronized (SisterCacheUtil.class) {
				if (instance == null) {
					instance = new SisterCacheUtil();
				}
			}

			mDaoSession = AppAplication.getDaoSession(context);
			mPictureCacheDao = mDaoSession.getSisterCacheDao();
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

		QueryBuilder<SisterCache> query = mPictureCacheDao.queryBuilder().where(SisterCacheDao
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
		SisterCache pictureCache = new SisterCache();
		pictureCache.setResult(result);
		pictureCache.setPage(page);
		pictureCache.setTime(System.currentTimeMillis());

		mPictureCacheDao.insert(pictureCache);
	}

}

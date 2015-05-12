package com.socks.jiandan.cache;

import android.content.Context;

import com.socks.greendao.FreshNewsCache;
import com.socks.greendao.FreshNewsCacheDao;
import com.socks.jiandan.base.AppAplication;
import com.socks.jiandan.model.FreshNews;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class FreshNewsCacheUtil extends BaseCacheUtil {

	private static FreshNewsCacheUtil instance;
	private static FreshNewsCacheDao mFreshNewsCacheDao;

	private FreshNewsCacheUtil() {
	}

	public static FreshNewsCacheUtil getInstance(Context context) {

		if (instance == null) {

			synchronized (FreshNewsCacheUtil.class) {
				if (instance == null) {
					instance = new FreshNewsCacheUtil();
				}
			}

			mDaoSession = AppAplication.getDaoSession(context);
			mFreshNewsCacheDao = mDaoSession.getFreshNewsCacheDao();
		}
		return instance;
	}

	/**
	 * 清楚全部缓存
	 */
	public void clearAllCache() {
		mFreshNewsCacheDao.deleteAll();
	}

	/**
	 * 根据页码获取缓存数据
	 *
	 * @param page
	 * @return
	 */
	@Override
	public ArrayList<FreshNews> getCacheByPage(int page) {

		QueryBuilder<FreshNewsCache> query = mFreshNewsCacheDao.queryBuilder().where(FreshNewsCacheDao
				.Properties.Page.eq("" + page));

		if (query.list().size() > 0) {
			try {
				return FreshNews.parseCache(new JSONArray(query.list().get(0)
						.getResult()));
			} catch (JSONException e) {
				e.printStackTrace();
				return new ArrayList<FreshNews>();
			}
		} else {
			return new ArrayList<FreshNews>();
		}

	}

	/**
	 * 添加FreshNews缓存
	 *
	 * @param result
	 * @param page
	 */
	@Override
	public void addResultCache(String result, int page) {

		FreshNewsCache freshNewsCache = new FreshNewsCache();
		freshNewsCache.setResult(result);
		freshNewsCache.setPage(page);
		freshNewsCache.setTime(System.currentTimeMillis());

		mFreshNewsCacheDao.insert(freshNewsCache);
	}

}

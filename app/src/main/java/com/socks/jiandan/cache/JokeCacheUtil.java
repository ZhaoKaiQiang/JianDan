package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.JokeCache;
import com.socks.greendao.JokeCacheDao;
import com.socks.jiandan.base.AppAplication;
import com.socks.jiandan.model.Joke;
import com.socks.jiandan.net.JSONParser;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class JokeCacheUtil extends BaseCacheUtil {

	private static JokeCacheUtil instance;
	private static JokeCacheDao mJokeCacheDao;

	private JokeCacheUtil() {
	}

	public static JokeCacheUtil getInstance(Context context) {

		if (instance == null) {

			synchronized (JokeCacheUtil.class) {
				if (instance == null) {
					instance = new JokeCacheUtil();
				}
			}

			mDaoSession = AppAplication.getDaoSession(context);
			mJokeCacheDao = mDaoSession.getJokeCacheDao();
		}
		return instance;
	}

	/**
	 * 清楚全部缓存
	 */
	public void clearAllCache() {
		mJokeCacheDao.deleteAll();
	}

	/**
	 * 根据页码获取缓存数据
	 *
	 * @param page
	 * @return
	 */
	@Override
	public ArrayList<Joke> getCacheByPage(int page) {
		QueryBuilder<JokeCache> query = mJokeCacheDao.queryBuilder().where(JokeCacheDao.Properties.Page.eq("" + page));

		if (query.list().size() > 0) {
			return (ArrayList<Joke>) JSONParser.toObject(query.list().get(0).getResult(),
					new TypeToken<ArrayList<Joke>>() {
					}.getType());
		} else {
			return new ArrayList<Joke>();
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
		JokeCache jokeCache = new JokeCache();
		jokeCache.setResult(result);
		jokeCache.setPage(page);
		jokeCache.setTime(System.currentTimeMillis());

		mJokeCacheDao.insert(jokeCache);
	}

}

package com.socks.jiandan.cache;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.socks.greendao.DaoSession;
import com.socks.greendao.JokeCache;
import com.socks.greendao.JokeCacheDao;
import com.socks.jiandan.base.AppAplication;
import com.socks.jiandan.model.Joke;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.utils.TextUtil;
import com.socks.jiandan.utils.logger.Logger;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */
public class JokeCacheUtil {

	/**
	 * 默认网络情况(4G，3G，2G)下，刷新间隔为3分钟，若是WIFI状态，则每次进入都刷新，无网络情况下，只加载缓存
	 */
	public static final long REFRESH_DISTANCE_TIME = 3 * 60 * 1000;

	public enum LoadType {
		WIFI, NG, NO
	}

	private static JokeCacheUtil instance;
	private static DaoSession mDaoSession;

	private static JokeCacheDao mJokeCacheDao;

	private JokeCacheUtil() {
	}

	public static JokeCacheUtil getInstance(Context context) {

		if (instance == null) {
			instance = new JokeCacheUtil();
		}

		mDaoSession = AppAplication.getDaoSession(context);
		mJokeCacheDao = mDaoSession.getJokeCacheDao();

		return instance;
	}

	/**
	 * 清楚全部缓存
	 */
	public void clearAllCache() {
		mDaoSession.clear();
	}

	/**
	 * 根据页码获取缓存数据
	 *
	 * @param page
	 * @return
	 */
	public ArrayList<Joke> getJokesByPage(int page) {
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
	 * 获取首页的缓存数据
	 *
	 * @return
	 */
	public ArrayList<Joke> getFirstPage() {
		return getJokesByPage(1);
	}

	/**
	 * 获取最近一次的刷新时间
	 *
	 * @return
	 */
	@Deprecated
	public long getRefreshTime() {

		QueryBuilder<JokeCache> query = mJokeCacheDao.queryBuilder().where(JokeCacheDao
				.Properties.Page.eq("1"));

		if (query.list().size() > 0) {
			JokeCache jokeCache = query.list().get(0);
			if (!TextUtil.isNull(jokeCache.getResult())) {
				Date date = new Date(jokeCache.getTime());
				Logger.d("获取刷新时间  time = " + jokeCache.getTime() + " date = " + date);
				return jokeCache.getTime();
			} else {
				//如果没有数据，则默认是REFRESH_DISTANCE_TIME*2时间之前刷新的
				return System.currentTimeMillis() - REFRESH_DISTANCE_TIME * 2;
			}
		} else {
			//如果没有数据，则默认是REFRESH_DISTANCE_TIME*2时间之前刷新的
			return System.currentTimeMillis() - REFRESH_DISTANCE_TIME * 2;
		}

	}

	/**
	 * 添加Jokes缓存
	 *
	 * @param result
	 * @param page
	 */
	public void addJokes(String result, int page) {

		JokeCache jokeCache = new JokeCache();
		jokeCache.setResult(result);
		jokeCache.setPage(page);
		jokeCache.setTime(System.currentTimeMillis());

		mJokeCacheDao.insert(jokeCache);
	}
}

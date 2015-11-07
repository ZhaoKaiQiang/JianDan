package com.socks.jiandan.cache;

import com.socks.greendao.DaoSession;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/5/12.
 */
public abstract class BaseCache<T> {

	public static final String DB_NAME = "jiandan-db";

	protected static DaoSession mDaoSession;

	public abstract void clearAllCache();

	public abstract ArrayList<T> getCacheByPage(int page);

	public abstract void addResultCache(String result, int page);

}

package com.socks;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 用来为GreenDao框架生成Dao文件
 */
public class MyDaoGenerator {

	//辅助文件生成的相对路径
	public static final String DAO_PATH = "../app/src/main/java-gen";
	//辅助文件的包名
	public static final String PACKAGE_NAME = "com.socks.greendao";
	//数据库的版本号
	public static final int DATA_VERSION_CODE = 1;

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(DATA_VERSION_CODE, PACKAGE_NAME);
		addCache(schema, "JokeCache");
		addCache(schema, "FreshNewsCache");
		addCache(schema, "PictureCache");
		addCache(schema, "SisterCache");
		addCache(schema, "VideoCache");
		//生成Dao文件路径
		new DaoGenerator().generateAll(schema, DAO_PATH);

	}

	/**
	 * 添加不同的缓存表
	 * @param schema
	 * @param tableName
	 */
	private static void addCache(Schema schema, String tableName) {

		Entity joke = schema.addEntity(tableName);

		//主键id自增长
		joke.addIdProperty().primaryKey().autoincrement();
		//请求结果
		joke.addStringProperty("result");
		//页数
		joke.addIntProperty("page");
		//插入时间，暂时无用
		joke.addLongProperty("time");

	}

}
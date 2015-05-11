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

	public static final String DAO_PATH = "../app/src/main/java-gen";
	public static final String PACKAGE_NAME = "com.socks.greendao";
	public static final int DATA_VERSION_CODE = 1;

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(DATA_VERSION_CODE, PACKAGE_NAME);
		addJoke(schema);
		//生成Dao文件路径
		new DaoGenerator().generateAll(schema, DAO_PATH);

	}

	/**
	 * 段子
	 *
	 * @param schema
	 */
	private static void addJoke(Schema schema) {

		Entity joke = schema.addEntity("JokeCache");

		joke.addIdProperty().primaryKey().autoincrement();
		//请求结果
		joke.addStringProperty("result");
		//页数
		joke.addIntProperty("page");
		joke.addLongProperty("time");

	}

}
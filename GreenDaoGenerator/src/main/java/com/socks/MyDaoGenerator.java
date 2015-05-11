package com.socks;

/**
 * Created by zhaokaiqiang on 15/5/11.
 */

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 用来为GreenDao框架生成Dao类
 */
public class MyDaoGenerator {

	public static final String DAO_PATH = "../app/src/main/java-gen";
	public static final String PACKAGE_NAME = "com.socks.greendao";
	public static final int VERSION_NAME = 1;

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(VERSION_NAME, PACKAGE_NAME);
		addNote(schema);
		new DaoGenerator().generateAll(schema, DAO_PATH);

	}

	private static void addNote(Schema schema) {
		Entity note = schema.addEntity("Note");
		note.addIdProperty();
		note.addStringProperty("text").notNull();
		note.addStringProperty("comment");
		note.addDateProperty("date");
	}

}
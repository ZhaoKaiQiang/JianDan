package com.socks.jiandan.utils;

/**
 * Created by zhaokaiqiang on 15/4/12.
 */
public class TextUtil {

	public static boolean isNull(String str) {

		if (str == null || str.length() == 0 || str.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

}

package com.socks.jiandan.utils;

/**
 * Created by zhaokaiqiang on 15/4/12.
 */
public class TextUtil {

	public static boolean isNull(CharSequence... str) {

		for (CharSequence cha : str) {
			if (cha == null || cha.length() == 0 || cha.equals("null")) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

}

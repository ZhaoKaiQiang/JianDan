package com.socks.jiandan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaokaiqiang on 15/4/12.
 */
public class TextUtil {

	public static final String REG_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";


	public static boolean isEmail(String email) {
		Pattern p = Pattern.compile(REG_EMAIL);
		Matcher m = p.matcher(email);
		return m.matches();
	}


	/**
	 * 判断是否为null、空字符串或者是"null"
	 *
	 * @param str
	 * @return
	 */
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

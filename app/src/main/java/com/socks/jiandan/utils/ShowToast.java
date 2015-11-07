/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：ShowToast.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-8-7
 */
package com.socks.jiandan.utils;

import android.widget.Toast;

import com.socks.jiandan.base.JDApplication;

/**
 * @author zhaokaiqiang
 * @ClassName: com.drd.piaojubao.utils.ShowToast
 * @Description: 显示Toast的工具类
 * @date 2014-8-7 上午11:21:48
 */
public class ShowToast {

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Description: 显示短时间Toast
	 */
	public static void Short(CharSequence sequence) {
		Toast.makeText(JDApplication.getContext(), sequence, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Description: 显示长时间Toast
	 */
	public static void Long(CharSequence sequence) {
		Toast.makeText(JDApplication.getContext(), sequence, Toast.LENGTH_SHORT).show();
	}

}

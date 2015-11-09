/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：EditTextShakeHelper.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-11-21
 */

package com.socks.jiandan.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class EditTextShakeHelper {

    // 震动动画
    private Animation shakeAnimation;
    // 插值器
    private CycleInterpolator cycleInterpolator;
    // 振动器
    private Vibrator shakeVibrator;

    public EditTextShakeHelper(Context context) {

        // 初始化振动器
        shakeVibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        // 初始化震动动画
        shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(300);
        cycleInterpolator = new CycleInterpolator(8);
        shakeAnimation.setInterpolator(cycleInterpolator);

    }

    public void shake(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.startAnimation(shakeAnimation);
        }

        shakeVibrator.vibrate(new long[]{0, 500}, -1);

    }

}

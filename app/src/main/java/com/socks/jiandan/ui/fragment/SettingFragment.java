package com.socks.jiandan.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.R;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.utils.AppInfoUtil;
import com.socks.jiandan.utils.FileUtil;
import com.socks.jiandan.utils.ShowToast;

import java.io.File;
import java.text.DecimalFormat;

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public static final String CLEAR_CACHE = "clear_cache";
    public static final String ABOUT_APP = "about_app";
    public static final String APP_VERSION = "app_version";
    public static final String ENABLE_SISTER = "enable_sister";
    public static final String ENABLE_FRESH_BIG = "enable_fresh_big";

    private Preference clearCache;
    private Preference aboutApp;
    private Preference appVersion;
    private CheckBoxPreference enableSister;
    private CheckBoxPreference enableBig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        clearCache = findPreference(CLEAR_CACHE);
        aboutApp = findPreference(ABOUT_APP);
        appVersion = findPreference(APP_VERSION);
        enableSister = (CheckBoxPreference) findPreference(ENABLE_SISTER);
        enableBig = (CheckBoxPreference) findPreference(ENABLE_FRESH_BIG);

        appVersion.setTitle(AppInfoUtil.getVersionName(getActivity()));

        File cacheFile = ImageLoader.getInstance().getDiskCache().getDirectory();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        clearCache.setSummary("缓存大小：" + decimalFormat.format(FileUtil.getDirSize(cacheFile)) + "M");

        enableSister.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ShowToast.Short(((Boolean) newValue) ? "已解锁隐藏属性->妹子图" : "已关闭隐藏属性->妹子图");
                return true;
            }
        });

        enableBig.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ShowToast.Short(((Boolean) newValue) ? "已开启大图模式" : "已关闭大图模式");
                return true;
            }
        });

        clearCache.setOnPreferenceClickListener(this);
        aboutApp.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();

        if (key.equals(CLEAR_CACHE)) {
            ImageLoader.getInstance().clearDiskCache();
            ShowToast.Short("清除缓存成功");
            clearCache.setSummary("缓存大小：0.00M");
        } else if (key.equals(ABOUT_APP)) {

            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("煎蛋开源版")
                    .backgroundColor(getResources().getColor(JDApplication.COLOR_OF_DIALOG))
                    .contentColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                    .positiveColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                    .negativeColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                    .neutralColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                    .titleColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                    .content("我是凯子哥，热爱分享，欢迎star ^_^")
                    .positiveText("GitHub")
                    .negativeText("WeiBo")
                    .neutralText("CSDN")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ZhaoKaiQiang/JianDan")));
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/zhaokaiqiang1992")));
                        }

                        @Override
                        public void onNeutral(MaterialDialog dialog) {
                            super.onNeutral(dialog);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/zhaokaiqiang1992")));
                        }
                    })
                    .build();
            dialog.show();
        }
        return true;
    }


}

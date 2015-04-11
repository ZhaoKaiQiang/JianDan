package com.socks.jiandan.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.socks.jiandan.R;
import com.socks.jiandan.ui.fragment.JokeFragment;
import com.socks.jiandan.ui.fragment.MainMenuFragment;
import com.socks.jiandan.utils.AppManager;
import com.socks.jiandan.utils.ScreenSizeUtil;
import com.socks.jiandan.utils.ShowToast;

public class MainActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	public void initView() {
		//为ActionBar添加点击事件
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenSizeUtil.getScreenWidth
				(this) * 4 / 7,
				LinearLayout.LayoutParams.MATCH_PARENT));
		linearLayout.setBackgroundColor(Color.TRANSPARENT);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setCustomView(linearLayout);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name,
				R.string.app_name) {

			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

		replaceFragment(R.id.frame_container, new JokeFragment());
		replaceFragment(R.id.drawer_container, new MainMenuFragment());
	}

	public void replaceFragment(int id_content, Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(id_content, fragment);
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// 如果我们想使用ActionBarDrawerToggle，我们必须在onPostCreate()和onConfigurationChanged()方法里面调用下面的方法
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void closeDrawer(){
		mDrawerLayout.closeDrawers();
	}


	// 用来计算返回键的点击间隔时间
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				ShowToast.Short("再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				// 彻底退出程序
				AppManager.getAppManager().finishAllActivityAndExit(this);
				finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}

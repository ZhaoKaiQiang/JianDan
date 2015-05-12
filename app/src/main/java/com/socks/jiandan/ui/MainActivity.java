package com.socks.jiandan.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.socks.jiandan.R;
import com.socks.jiandan.base.Initialable;
import com.socks.jiandan.model.NetWorkEvent;
import com.socks.jiandan.ui.fragment.FreshNewsFragment;
import com.socks.jiandan.ui.fragment.MainMenuFragment;
import com.socks.jiandan.utils.AppManager;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ScreenSizeUtil;
import com.socks.jiandan.utils.ShowToast;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity implements Initialable {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private BroadcastReceiver netStateReceiver;
	private MaterialDialog noNetWorkDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	@Override
	public void initView() {

		//为ActionBar添加点击事件
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenSizeUtil.getScreenWidth
				(this) * 1 / 2,
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

		replaceFragment(R.id.frame_container, new FreshNewsFragment());
		replaceFragment(R.id.drawer_container, new MainMenuFragment());
	}

	@Override
	public void initData() {

		netStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						ConnectivityManager.CONNECTIVITY_ACTION)) {
					if (NetWorkUtil.isNetWorkConnected(MainActivity.this)) {
						EventBus.getDefault().post(new NetWorkEvent(NetWorkEvent.AVAILABLE));
					} else {
						EventBus.getDefault().post(new NetWorkEvent(NetWorkEvent.UNAVAILABLE));
					}
				}
			}
		};

		//注册网络状态广播接受者
		registerReceiver(netStateReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	public void onEvent(NetWorkEvent event) {

		if (event.getType() == NetWorkEvent.UNAVAILABLE) {

			if (noNetWorkDialog == null) {
				noNetWorkDialog = new MaterialDialog.Builder(MainActivity.this)
						.title("无网络连接")
						.content("去开启网络?")
						.positiveText("是")
						.negativeText("否")
						.callback(new MaterialDialog.ButtonCallback() {
							@Override
							public void onPositive(MaterialDialog dialog) {
								Intent intent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}

							@Override
							public void onNegative(MaterialDialog dialog) {
							}
						})
						.cancelable(false)
						.build();
			}

			if (!noNetWorkDialog.isShowing()) {
				noNetWorkDialog.show();
			}

		}

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

	public void closeDrawer() {
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
				AppManager.getAppManager().finishAllActivityAndExit(this);
				finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netStateReceiver);
	}
}

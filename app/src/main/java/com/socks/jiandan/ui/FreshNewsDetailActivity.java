package com.socks.jiandan.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.ui.fragment.FreshNewsDetailFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FreshNewsDetailActivity extends BaseActivity {

	@InjectView(R.id.vp)
	ViewPager vp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fresh_news_detail);
		ButterKnife.inject(this);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		ArrayList<FreshNews> FreshNews = (ArrayList<FreshNews>) getIntent().getSerializableExtra
				("FreshNews");

		int position = getIntent().getIntExtra("position", 0);
		vp.setAdapter(new FreshNewsDetailAdapter(getSupportFragmentManager(), FreshNews));
		vp.setCurrentItem(position);

	}


	private class FreshNewsDetailAdapter extends FragmentPagerAdapter {

		private ArrayList<FreshNews> freshNewses;

		public FreshNewsDetailAdapter(FragmentManager fm, ArrayList<FreshNews> freshNewses) {
			super(fm);
			this.freshNewses = freshNewses;
		}

		@Override
		public Fragment getItem(int position) {
			return FreshNewsDetailFragment.getInstance(freshNewses.get(position));
		}

		@Override
		public int getCount() {
			return freshNewses.size();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

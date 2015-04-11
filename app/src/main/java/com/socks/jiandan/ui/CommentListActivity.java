package com.socks.jiandan.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.net.Request4CommentList;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.SwipeBackUtil;
import com.socks.jiandan.view.googleprogressbar.GoogleProgressBar;
import com.socks.jiandan.view.matchview.MatchTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentListActivity extends BaseActivity {


	@InjectView(R.id.swipe_refresh)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@InjectView(R.id.recycler_view)
	RecyclerView mRecyclerView;
	@InjectView(R.id.google_progress)
	GoogleProgressBar google_progress;
	@InjectView(R.id.tv_no_thing)
	MatchTextView tv_no_thing;

	private String thread_key;
	private CommentAdapter mAdapter;
	private SwipeBackUtil mSwipeBackUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
		initView();
		initData();
	}

	@Override
	public void initView() {

		ButterKnife.inject(this);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		mSwipeBackUtil = new SwipeBackUtil(this);

		mRecyclerView.setHasFixedSize(false);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mAdapter.loadData();
			}
		});


	}

	@Override
	public void initData() {

		thread_key = getIntent().getStringExtra("thread_key");

		if (TextUtils.isEmpty(thread_key) || thread_key.equals("0")) {
			ShowToast.Short("禁止评论");
			finish();
		}

		mAdapter = new CommentAdapter();
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.loadData();

	}

	private class CommentAdapter extends RecyclerView.Adapter<ViewHolder> {

		private ArrayList<Commentator> commentators;

		public CommentAdapter() {
			commentators = new ArrayList<>();
		}


		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = getLayoutInflater().inflate(R.layout.item_comment, parent,
					false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {

			Commentator commentator = commentators.get(position);
			holder.tv_name.setText(commentator.getName());
			holder.tv_content.setText(commentator.getMessage());
			String timeString = commentator.getCreated_at().replace("T", " ");
			timeString = timeString.substring(0, timeString.indexOf("+"));
			holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(timeString));
			holder.ll_vote.setVisibility(View.GONE);

		}

		@Override
		public int getItemCount() {
			return commentators.size();
		}


		@Override
		public int getItemViewType(int position) {
			return super.getItemViewType(position);
		}

		public void loadData() {
			executeRequest(new Request4CommentList(Commentator.getUrlCommentList(thread_key), new Response
					.Listener<ArrayList<Commentator>>() {
				@Override
				public void onResponse(ArrayList<Commentator> response) {

					google_progress.setVisibility(View.GONE);
					if (response.size() == 0) {
						tv_no_thing.setVisibility(View.VISIBLE);
					} else {
						commentators.clear();
						commentators.addAll(response);
						mAdapter.notifyDataSetChanged();
					}
					mSwipeRefreshLayout.setRefreshing(false);

				}
			}, errorListener()));
		}


	}

	private static class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tv_name;
		private TextView tv_content;
		private TextView tv_time;
		private LinearLayout ll_vote;

		public ViewHolder(View itemView) {
			super(itemView);
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			tv_content = (TextView) itemView.findViewById(R.id.tv_content);
			tv_time = (TextView) itemView.findViewById(R.id.tv_time);
			ll_vote = (LinearLayout) itemView.findViewById(R.id.ll_vote);

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_comment_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_edit:
				ShowToast.Short("发表评论");
				return true;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mSwipeBackUtil.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}
}

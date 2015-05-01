package com.socks.jiandan.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.net.Request4CommentList;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.SwipeBackUtil;
import com.socks.jiandan.view.floorview.FloorView;
import com.socks.jiandan.view.floorview.SubComments;
import com.socks.jiandan.view.floorview.SubFloorFactory;
import com.socks.jiandan.view.googleprogressbar.GoogleProgressBar;
import com.socks.jiandan.view.matchview.MatchTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	@InjectView(R.id.tv_error)
	MatchTextView tv_error;

	private String thread_key;
	private String thread_id;
	private CommentAdapter mAdapter;
	private SwipeBackUtil mSwipeBackUtil;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

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

		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.ic_loading_small)
				.build();

	}

	@Override
	public void initData() {

		tv_no_thing.setVisibility(View.GONE);
		google_progress.setVisibility(View.VISIBLE);
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

			switch (viewType) {
				case Commentator.TYPE_HOT:
				case Commentator.TYPE_NEW:
					return new ViewHolder(getLayoutInflater().inflate(R.layout
									.item_comment_flag, parent,
							false));
				case Commentator.TYPE_NORMAL:
					return new ViewHolder(getLayoutInflater().inflate(R.layout.item_comment, parent,
							false));
				default:
					return null;
			}
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {

			final Commentator commentator = commentators.get(position);

			switch (commentator.getType()) {
				case Commentator.TYPE_HOT:
					holder.tv_flag.setText("热门评论");
					break;
				case Commentator.TYPE_NEW:
					holder.tv_flag.setText("最新评论");
					break;
				case Commentator.TYPE_NORMAL:
					holder.tv_name.setText(commentator.getName());
					holder.tv_content.setText(commentator.getMessage());

					holder.tv_content.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							new MaterialDialog.Builder(CommentListActivity.this)
									.title(commentator.getName())
									.items(R.array.comment_dialog)
									.itemsCallback(new MaterialDialog.ListCallback() {
										@Override
										public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

											switch (which) {
												//评论
												case 0:
													Intent intent = new Intent
															(CommentListActivity.this,
																	PushCommentActivity.class);
													intent.putExtra("parent_id", commentator.getPost_id());
													intent.putExtra("thread_id", thread_id);
													intent.putExtra("parent_name", commentator
															.getName());
													startActivityForResult(intent, 0);
													break;
												case 1:
													//复制到剪贴板
													ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
													clip.setPrimaryClip(ClipData.newPlainText
															(null, commentator.getMessage()));
													ShowToast.Short(ToastMsg.COPY_SUCCESS);
													break;
											}

										}
									})
									.show();

						}
					});

					String timeString = commentator.getCreated_at().replace("T", " ");
					timeString = timeString.substring(0, timeString.indexOf("+"));
					holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(timeString));
					holder.ll_vote.setVisibility(View.GONE);

					//有楼层,盖楼
					if (commentator.getFloorNum() > 1) {

						SubComments cmts = new SubComments(addFloors(commentator));
						holder.floors_parent.setComments(cmts);
						holder.floors_parent.setFactory(new SubFloorFactory());
						holder.floors_parent.setBoundDrawer(getResources().getDrawable(
								R.drawable.bg_comment));
						holder.floors_parent.init();

					} else {
						holder.floors_parent.setVisibility(View.GONE);
					}

					imageLoader.displayImage(commentator.getAvatar_url(), holder.img_header);

					break;
			}

		}

		private List<Commentator> addFloors(Commentator commentator) {

			//只有一层
			if (commentator.getFloorNum() == 1) {
				return null;
			}

			List<String> parentIds = Arrays.asList(commentator.getParents());
			ArrayList<Commentator> commentators = new ArrayList<>();

			for (Commentator comm : this.commentators) {

				if (parentIds.contains(comm.getPost_id())) {
					commentators.add(comm);
				}

			}

			Collections.reverse(commentators);

			return commentators;

		}

		@Override
		public int getItemCount() {
			return commentators.size();
		}

		@Override
		public int getItemViewType(int position) {
			return commentators.get(position).getType();
		}

		public void loadData() {
			executeRequest(new Request4CommentList(Commentator.getUrlCommentList(thread_key), new Response
					.Listener<ArrayList<Commentator>>() {
				@Override
				public void onResponse(ArrayList<Commentator> response) {

					google_progress.setVisibility(View.GONE);
					tv_error.setVisibility(View.GONE);

					if (response.size() == 0) {
						tv_no_thing.setVisibility(View.VISIBLE);
					} else {
						commentators.clear();

						ArrayList<Commentator> hotCommentator = new ArrayList<>();
						ArrayList<Commentator> normalComment = new ArrayList<>();

						//添加热门评论
						for (Commentator commentator : response) {
							if (commentator.getTag().equals(Commentator.TAG_HOT)) {
								hotCommentator.add(commentator);
							} else {
								normalComment.add(commentator);
							}
						}

						//添加热门评论标签
						if (hotCommentator.size() != 0) {
							Collections.sort(hotCommentator);
							Commentator hotCommentFlag = new Commentator();
							hotCommentFlag.setType(Commentator.TYPE_HOT);
							hotCommentator.add(0, hotCommentFlag);
							commentators.addAll(hotCommentator);
						}

						//添加最新评论及标签
						if (normalComment.size() != 0) {
							Commentator newCommentFlag = new Commentator();
							newCommentFlag.setType(Commentator.TYPE_NEW);
							commentators.add(newCommentFlag);
							Collections.sort(normalComment);
							commentators.addAll(normalComment);
						}

						mAdapter.notifyDataSetChanged();
					}
					mSwipeRefreshLayout.setRefreshing(false);

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					mSwipeRefreshLayout.setRefreshing(false);
					google_progress.setVisibility(View.GONE);
					tv_error.setVisibility(View.VISIBLE);
					tv_no_thing.setVisibility(View.GONE);
				}
			}, new LoadFinishCallBack() {
				@Override
				public void loadFinish(Object obj) {
					thread_id = (String) obj;
				}
			}));
		}


	}

	private static class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tv_name;
		private TextView tv_content;
		private TextView tv_time;
		private LinearLayout ll_vote;
		private ImageView img_header;
		private FloorView floors_parent;

		private TextView tv_flag;

		public ViewHolder(View itemView) {
			super(itemView);
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			tv_content = (TextView) itemView.findViewById(R.id.tv_content);
			tv_time = (TextView) itemView.findViewById(R.id.tv_time);
			ll_vote = (LinearLayout) itemView.findViewById(R.id.ll_vote);
			img_header = (ImageView) itemView.findViewById(R.id.img_header);
			floors_parent = (FloorView) itemView.findViewById(R.id.floors_parent);

			tv_flag = (TextView) itemView.findViewById(R.id.tv_flag);

			setIsRecyclable(false);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			initData();
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
				Intent intent = new Intent(this, PushCommentActivity.class);
				intent.putExtra("thread_id", thread_id);
				startActivityForResult(intent, 0);
				return true;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mSwipeBackUtil.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}
}

package com.socks.jiandan.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.Comment4FreshNews;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.model.Vote;
import com.socks.jiandan.net.Request4FreshNewsCommentList;
import com.socks.jiandan.net.Request4Vote;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.SwipeBackUtil;
import com.socks.jiandan.view.floorview.FloorView;
import com.socks.jiandan.view.floorview.SubComments;
import com.socks.jiandan.view.floorview.SubFloorFactory;
import com.socks.jiandan.view.googleprogressbar.GoogleProgressBar;
import com.socks.jiandan.view.matchview.MatchTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 新鲜事评论列表页
 */
public class CommentList4FreshNewsActivity extends BaseActivity {

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

	private String thread_id;
	private String parent_id;
	private String parent_name;
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
		actionBar.setDisplayShowTitleEnabled(false);

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

		tv_no_thing.setVisibility(View.GONE);
		google_progress.setVisibility(View.VISIBLE);
		thread_id = getIntent().getStringExtra("id");

		if (TextUtils.isEmpty(thread_id) || thread_id.equals("0")) {
			ShowToast.Short("禁止评论");
			finish();
		}

		mAdapter = new CommentAdapter();
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.loadData();

	}

	private class CommentAdapter extends RecyclerView.Adapter<ViewHolder> {

		private ArrayList<Comment4FreshNews> commentators;

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

			final Comment4FreshNews commentator = commentators.get(position);

			switch (commentator.getType()) {
				case Comment4FreshNews.TYPE_HOT:
					holder.tv_flag.setText("热门评论");
					break;
				case Comment4FreshNews.TYPE_NEW:
					holder.tv_flag.setText("最新评论");
					break;
				case Commentator.TYPE_NORMAL:

					holder.ll_left.setVisibility(View.GONE);

					holder.tv_like.setText(commentator.getVote_positive() + "");
					holder.tv_unlike.setText(commentator.getVote_negative() + "");

					holder.tv_name.setText(commentator.getName());
					holder.tv_content.setText(commentator.getContent().trim());
					holder.tv_content.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							new MaterialDialog.Builder(CommentList4FreshNewsActivity.this)
									.title(commentator.getName())
									.items(R.array.comment_dialog)
									.itemsCallback(new MaterialDialog.ListCallback() {
										@Override
										public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

											switch (which) {
												//评论
												case 0:
													Intent intent = new Intent
															(CommentList4FreshNewsActivity.this,
																	PushCommentActivity.class);
													intent.putExtra("parent_id",String.valueOf(commentator.getId()));
													intent.putExtra("thread_id", thread_id);
													intent.putExtra("parent_name", commentator.getName());
													startActivityForResult(intent, 0);
													break;
												case 1:
													//复制到剪贴板
													ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
													clip.setPrimaryClip(ClipData.newPlainText
															(null, commentator.getContent()));
													ShowToast.Short(ToastMsg.COPY_SUCCESS);
													break;
											}

										}
									})
									.show();


						}
					});

					holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(commentator.getDate()));

					//有楼层,盖楼
					if (commentator.getFloorNum() > 1) {

						SubComments cmts = new SubComments(commentator.getParentComments());
						holder.floors_parent.setComments(cmts);
						holder.floors_parent.setFactory(new SubFloorFactory());
						holder.floors_parent.setBoundDrawer(getResources().getDrawable(
								R.drawable.bg_comment));
						holder.floors_parent.init();

					} else {
						holder.floors_parent.setVisibility(View.GONE);
					}

					holder.ll_support.setOnClickListener(new onVoteClickListener(commentator.getId()+"",
							Vote.OO, holder,commentator));

					holder.ll_unsupport.setOnClickListener(new onVoteClickListener(commentator.getId()+"",
							Vote.XX, holder,commentator));

					break;
			}

		}

		/**
		 * 投票监听器
		 */
		private class onVoteClickListener implements View.OnClickListener {

			private String comment_ID;
			private String tyle;
			private ViewHolder holder;
			private Comment4FreshNews commentator;

			public onVoteClickListener(String comment_ID, String tyle, ViewHolder holder,Comment4FreshNews commentator) {
				this.comment_ID = comment_ID;
				this.tyle = tyle;
				this.holder = holder;
				this.commentator=commentator;
			}


			@Override
			public void onClick(View v) {

				//避免快速点击，造成大量网络访问
				if (holder.isClickFinish) {
					vote(comment_ID, tyle, holder,commentator);
					holder.isClickFinish = false;
				}

			}
		}

		/**
		 * 投票
		 *
		 */
		public void vote(String comment_ID, String style, final ViewHolder holder,final Comment4FreshNews commentator) {

			String url;

			if (style.equals(Vote.XX)) {
				url = Vote.getXXUrl(comment_ID);
			} else {
				url = Vote.getOOUrl(comment_ID);
			}

			executeRequest(new Request4Vote(url, new
					Response.Listener<Vote>() {
						@Override
						public void onResponse(Vote response) {

							holder.isClickFinish = true;
							String result = response.getResult();

							if (result.equals(Vote.RESULT_OO_SUCCESS)) {
								ShowToast.Short("顶的好舒服~");
								//变红+1
								int vote = commentator.getVote_positive();
								commentator.setVote_positive(vote + 1);
								holder.tv_like.setText(commentator.getVote_positive() + "");
								holder.tv_like.setTypeface(Typeface.DEFAULT_BOLD);
								holder.tv_like.setTextColor(getResources().getColor
										(android.R.color.holo_red_light));
								holder.tv_support_des.setTextColor(getResources().getColor
										(android.R.color.holo_red_light));

							} else if (result.equals(Vote.RESULT_XX_SUCCESS)) {
								ShowToast.Short("疼...轻点插");
								//变绿+1
								int vote = commentator.getVote_negative();
								commentator.setVote_negative(vote + 1);
								holder.tv_unlike.setText(commentator.getVote_negative()+"");
								holder.tv_unlike.setTypeface(Typeface.DEFAULT_BOLD);
								holder.tv_unlike.setTextColor(getResources().getColor
										(android.R.color.holo_green_light));
								holder.tv_unsupport_des.setTextColor(getResources().getColor
										(android.R.color.holo_green_light));

							} else if (result.equals(Vote.RESULT_HAVE_VOTED)) {
								ShowToast.Short("投过票了");
							} else {
								ShowToast.Short("卧槽，发生了什么！");
							}

						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					ShowToast.Short(ToastMsg.VOTE_FAILED);
					holder.isClickFinish = true;
				}
			}));
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
			executeRequest(new Request4FreshNewsCommentList(Comment4FreshNews.getUrlComments(thread_id), new Response
					.Listener<ArrayList<Comment4FreshNews>>() {
				@Override
				public void onResponse(ArrayList<Comment4FreshNews> response) {

					google_progress.setVisibility(View.GONE);
					tv_error.setVisibility(View.GONE);

					if (response.size() == 0) {
						tv_no_thing.setVisibility(View.VISIBLE);
					} else {
						commentators.clear();

						//如果评论条数大于6，就选择positive前6作为热门评论
						if (response.size() > 6) {
							Comment4FreshNews comment4FreshNews = new Comment4FreshNews();
							comment4FreshNews.setType(Comment4FreshNews.TYPE_HOT);
							commentators.add(comment4FreshNews);

							Collections.sort(response, new Comparator<Comment4FreshNews>() {
								@Override
								public int compare(Comment4FreshNews lhs, Comment4FreshNews rhs) {
									return lhs.getVote_positive() <= rhs.getVote_positive() ? 1 :
											-1;
								}
							});

							List<Comment4FreshNews> subComments = response.subList(0, 6);

							for (Comment4FreshNews subComment : subComments) {
								subComment.setTag(Comment4FreshNews.TAG_HOT);
							}

							commentators.addAll(subComments);

						}

						Comment4FreshNews comment4FreshNews = new Comment4FreshNews();
						comment4FreshNews.setType(Comment4FreshNews.TYPE_NEW);
						commentators.add(comment4FreshNews);

						Collections.sort(response);

						for (Comment4FreshNews comment4Normal : response) {
							if (comment4Normal.getTag().equals(Comment4FreshNews.TAG_NORMAL)) {
								commentators.add(comment4Normal);
							}
						}

					}

					mAdapter.notifyDataSetChanged();
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
		private TextView tv_like;
		private TextView tv_unlike;
		private TextView tv_time;
		private FloorView floors_parent;
		private LinearLayout ll_left;
		private TextView tv_flag;
		private LinearLayout ll_support;
		private LinearLayout ll_unsupport;
		private TextView tv_unsupport_des;
		private TextView tv_support_des;

		//用于处理多次点击造成的网络访问
		private boolean isClickFinish;

		public ViewHolder(View itemView) {
			super(itemView);
			isClickFinish = true;
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			tv_content = (TextView) itemView.findViewById(R.id.tv_content);
			tv_time = (TextView) itemView.findViewById(R.id.tv_time);
			tv_like = (TextView) itemView.findViewById(R.id.tv_like);
			tv_unlike = (TextView) itemView.findViewById(R.id.tv_unlike);
			ll_left = (LinearLayout) itemView.findViewById(R.id.ll_left);
			floors_parent = (FloorView) itemView.findViewById(R.id.floors_parent);
			ll_support = (LinearLayout) itemView.findViewById(R.id.ll_support);
			ll_unsupport = (LinearLayout) itemView.findViewById(R.id.ll_unsupport);
			tv_unsupport_des = (TextView) itemView.findViewById(R.id.tv_unlike_descr);
			tv_support_des = (TextView) itemView.findViewById(R.id.tv_like_descr);

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

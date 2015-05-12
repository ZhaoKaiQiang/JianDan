package com.socks.jiandan.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.cache.SisterCacheUtil;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.CommentNumber;
import com.socks.jiandan.model.NetWorkEvent;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.model.Vote;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.net.Request4CommentCounts;
import com.socks.jiandan.net.Request4Picture;
import com.socks.jiandan.net.Request4Vote;
import com.socks.jiandan.ui.CommentListActivity;
import com.socks.jiandan.ui.ImageDetailActivity;
import com.socks.jiandan.utils.FileUtil;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.TextUtil;
import com.socks.jiandan.view.AutoLoadRecyclerView;
import com.socks.jiandan.view.ShowMaxImageView;
import com.socks.jiandan.view.googleprogressbar.GoogleProgressBar;
import com.socks.jiandan.view.matchview.MatchTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * 妹子图碎片
 *
 * @author zhaokaiqiang
 */
public class SisterFragment extends BaseFragment {

	@InjectView(R.id.recycler_view)
	AutoLoadRecyclerView mRecyclerView;
	@InjectView(R.id.swipeRefreshLayout)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@InjectView(R.id.google_progress)
	GoogleProgressBar google_progress;
	@InjectView(R.id.tv_error)
	MatchTextView tv_error;

	private PictureAdapter mAdapter;
	private LoadFinishCallBack mLoadFinisCallBack;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private boolean isWifiConnected;
	//用于记录是否是首次进入
	private boolean isFirstChange;
	//记录最后一次提示显示时间，防止多次提示
	private long lastShowTime;

	public SisterFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mActionBar.setTitle("妹子图");
		isFirstChange = true;
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_joke, container, false);
		ButterKnife.inject(this, view);

		mRecyclerView.setHasFixedSize(false);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mLoadFinisCallBack = mRecyclerView;
		mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
			@Override
			public void loadMore() {
				mAdapter.loadNextPage();
			}
		});

		mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mAdapter.loadFirst();
			}
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		imageLoader = ImageLoader.getInstance();
		mRecyclerView.setOnPauseListenerParams(imageLoader, false, true);

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.ic_loading_large)
				.build();

		mAdapter = new PictureAdapter();
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.loadFirst();

	}

	public void onEventMainThread(NetWorkEvent event) {

		if (event.getType() == NetWorkEvent.AVAILABLE) {

			if (NetWorkUtil.isWifiConnected(getActivity())) {
				isWifiConnected = true;
				if (!isFirstChange && (System.currentTimeMillis() - lastShowTime) > 3000) {
					ShowToast.Short("已切换为WIFI模式，自动加载GIF图片");
					lastShowTime = System.currentTimeMillis();
				}

			} else {
				isWifiConnected = false;
				if (!isFirstChange && (System.currentTimeMillis() - lastShowTime) > 3000) {
					ShowToast.Short("已切换为省流量模式，只加载GIF缩略图");
					lastShowTime = System.currentTimeMillis();
				}
			}

			isFirstChange = false;
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//清除内存缓存，避免由于内存缓存造成的图片显示不完整
		imageLoader.clearMemoryCache();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_refresh, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_refresh) {
			mSwipeRefreshLayout.setRefreshing(true);
			mAdapter.loadFirst();
			return true;
		}

		return false;
	}

	@Override
	public void onActionBarClick() {
		if (mRecyclerView != null && mAdapter.pictures.size() > 0) {
			mRecyclerView.scrollToPosition(0);
		}
	}

	public class PictureAdapter extends RecyclerView.Adapter<ViewHolder> {

		private int page;
		private ArrayList<Picture> pictures;
		private int lastPosition = -1;

		public PictureAdapter() {
			pictures = new ArrayList<Picture>();
		}

		private void setAnimation(View viewToAnimate, int position) {
			if (position > lastPosition) {
				Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
						.anim.item_bottom_in);
				viewToAnimate.startAnimation(animation);
				lastPosition = position;
			}
		}

		@Override
		public void onViewDetachedFromWindow(ViewHolder holder) {
			super.onViewDetachedFromWindow(holder);
			holder.card.clearAnimation();
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent,
		                                     int viewType) {
			View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_pic, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {

			final Picture picture = pictures.get(position);

			String picUrl = picture.getPics()[0];

			holder.img_gif.setVisibility(View.GONE);
			holder.progress.setProgress(0);
			holder.progress.setVisibility(View.VISIBLE);

			imageLoader.displayImage(picUrl, holder.img, options, new
							SimpleImageLoadingListener() {
								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view, loadedImage);
									holder.progress.setVisibility(View.GONE);
								}
							},
					new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progress.setProgress((int) (current * 100f / total));
						}
					});

			if (TextUtil.isNull(picture.getText_content().trim())) {
				holder.tv_content.setVisibility(View.GONE);
			} else {
				holder.tv_content.setVisibility(View.VISIBLE);
				holder.tv_content.setText(picture.getText_content().trim());
			}


			holder.img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ImageDetailActivity.class);

					intent.putExtra("img_author", picture.getComment_author());
					intent.putExtra("img_url", picture.getPics());
					intent.putExtra("img_id", picture.getComment_ID());
					intent.putExtra("thread_key", "comment-" + picture.getComment_ID());
					intent.putExtra("is_need_webview", false);
					startActivity(intent);
				}
			});

			holder.tv_author.setText(picture.getComment_author());
			holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(picture.getComment_date()));
			holder.tv_like.setText(picture.getVote_positive());
			holder.tv_comment_count.setText(picture.getComment_counts());

			//用于恢复默认的文字
			holder.tv_like.setTypeface(Typeface.DEFAULT);
			holder.tv_like.setTextColor(getResources().getColor(R.color
					.secondary_text_default_material_light));
			holder.tv_support_des.setTextColor(getResources().getColor(R.color
					.secondary_text_default_material_light));

			holder.tv_unlike.setText(picture.getVote_negative());
			holder.tv_unlike.setTypeface(Typeface.DEFAULT);
			holder.tv_unlike.setTextColor(getResources().getColor(R.color
					.secondary_text_default_material_light));
			holder.tv_unsupport_des.setTextColor(getResources().getColor(R.color
					.secondary_text_default_material_light));

			holder.img_share.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new MaterialDialog.Builder(getActivity())
							.items(R.array.picture_dialog)
							.itemsCallback(new MaterialDialog.ListCallback() {
								@Override
								public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

									switch (which) {
										//分享
										case 0:
											ShareUtil.sharePicture(getActivity(), picture
													.getPics()[0]);
											break;
										//保存
										case 1:
											FileUtil.savePicture(getActivity(), picture
													.getPics()[0]);
											break;
									}

								}
							})
							.show();
				}
			});

			holder.ll_support.setOnClickListener(new onVoteClickListener(picture.getComment_ID(),
					Vote.OO, holder, picture));

			holder.ll_unsupport.setOnClickListener(new onVoteClickListener(picture.getComment_ID(),
					Vote.XX, holder, picture));

			holder.ll_comment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), CommentListActivity.class);
					intent.putExtra("thread_key", "comment-" + picture.getComment_ID());
					startActivity(intent);
				}
			});

			setAnimation(holder.card, position);

		}

		/**
		 * 投票监听器
		 */
		private class onVoteClickListener implements View.OnClickListener {

			private String comment_ID;
			private String tyle;
			private ViewHolder holder;
			private Picture picture;

			public onVoteClickListener(String comment_ID, String tyle, ViewHolder holder, Picture joke) {
				this.comment_ID = comment_ID;
				this.tyle = tyle;
				this.holder = holder;
				this.picture = joke;
			}

			@Override
			public void onClick(View v) {

				//避免快速点击，造成大量网络访问
				if (holder.isClickFinish) {
					vote(comment_ID, tyle, holder, picture);
					holder.isClickFinish = false;
				}

			}
		}

		/**
		 * 投票
		 *
		 * @param comment_ID
		 */
		public void vote(String comment_ID, String tyle, final ViewHolder holder, final Picture picture) {

			String url;

			if (tyle.equals(Vote.XX)) {
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
								ShowToast.Short(ToastMsg.VOTE_OO);
								//变红+1
								int vote = Integer.valueOf(picture.getVote_positive());
								picture.setVote_positive((vote + 1) + "");
								holder.tv_like.setText(picture.getVote_positive());
								holder.tv_like.setTypeface(Typeface.DEFAULT_BOLD);
								holder.tv_like.setTextColor(getResources().getColor
										(android.R.color.holo_red_light));
								holder.tv_support_des.setTextColor(getResources().getColor
										(android.R.color.holo_red_light));

							} else if (result.equals(Vote.RESULT_XX_SUCCESS)) {
								ShowToast.Short(ToastMsg.VOTE_XX);
								//变绿+1
								int vote = Integer.valueOf(picture.getVote_negative());
								picture.setVote_negative((vote + 1) + "");
								holder.tv_unlike.setText(picture.getVote_negative());
								holder.tv_unlike.setTypeface(Typeface.DEFAULT_BOLD);
								holder.tv_unlike.setTextColor(getResources().getColor
										(android.R.color.holo_green_light));
								holder.tv_unsupport_des.setTextColor(getResources().getColor
										(android.R.color.holo_green_light));

							} else if (result.equals(Vote.RESULT_HAVE_VOTED)) {
								ShowToast.Short(ToastMsg.VOTE_REPEAT);
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
			return pictures.size();
		}

		public void loadFirst() {
			page = 1;
			loadDataByNetworkType();
		}

		public void loadNextPage() {
			page++;
			loadDataByNetworkType();
		}

		private void loadDataByNetworkType() {

			if (NetWorkUtil.isNetWorkConnected(getActivity())) {
				loadData();
			} else {
				loadCache();
			}

		}

		private void loadData() {
			executeRequest(new Request4Picture(Picture.getRequestUrl(Picture.PictureType.Syster,
					page),
					new Response.Listener<ArrayList<Picture>>
							() {
						@Override
						public void onResponse(ArrayList<Picture> response) {
							getCommentCounts(response);
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

					ShowToast.Short(ToastMsg.LOAD_FAILED);
					google_progress.setVisibility(View.GONE);
					mLoadFinisCallBack.loadFinish(null);
					if (mSwipeRefreshLayout.isRefreshing()) {
						mSwipeRefreshLayout.setRefreshing(false);
					}
				}
			}));
		}

		/**
		 * 从缓存中加载
		 */
		private void loadCache() {

			google_progress.setVisibility(View.GONE);
			mLoadFinisCallBack.loadFinish(null);
			if (mSwipeRefreshLayout.isRefreshing()) {
				mSwipeRefreshLayout.setRefreshing(false);
			}

			SisterCacheUtil sisterCacheUtil = SisterCacheUtil.getInstance(getActivity());
			if (page == 1) {
				pictures.clear();
				ShowToast.Short(ToastMsg.LOAD_NO_NETWORK);
			}

			pictures.addAll(sisterCacheUtil.getCacheByPage(page));
			notifyDataSetChanged();

		}

		//获取评论数量
		private void getCommentCounts(final ArrayList<Picture> pictures) {

			StringBuilder sb = new StringBuilder();
			for (Picture joke : pictures) {
				sb.append("comment-" + joke.getComment_ID() + ",");
			}

			executeRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(sb.toString()), new Response
					.Listener<ArrayList<CommentNumber>>() {

				@Override
				public void onResponse(ArrayList<CommentNumber> response) {

					google_progress.setVisibility(View.GONE);
					tv_error.setVisibility(View.GONE);
					mLoadFinisCallBack.loadFinish(null);
					if (mSwipeRefreshLayout.isRefreshing()) {
						mSwipeRefreshLayout.setRefreshing(false);
					}

					for (int i = 0; i < pictures.size(); i++) {
						pictures.get(i).setComment_counts(response.get(i).getComments() + "");
					}

					if (page == 1) {
						PictureAdapter.this.pictures.clear();
						SisterCacheUtil.getInstance(getActivity()).clearAllCache();
					}

					PictureAdapter.this.pictures.addAll(pictures);
					notifyDataSetChanged();

					//加载完毕后缓存
					SisterCacheUtil.getInstance(getActivity()).addResultCache(JSONParser.toString
							(pictures), page);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					mLoadFinisCallBack.loadFinish(null);
					ShowToast.Short(ToastMsg.LOAD_FAILED);
					google_progress.setVisibility(View.GONE);
					if (mSwipeRefreshLayout.isRefreshing()) {
						mSwipeRefreshLayout.setRefreshing(false);
					}

				}
			}
			));

		}

	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tv_author;
		private TextView tv_time;
		private TextView tv_content;
		private TextView tv_like;
		private TextView tv_unlike;
		private TextView tv_comment_count;
		private TextView tv_unsupport_des;
		private TextView tv_support_des;

		private ImageView img_share;
		private ImageView img_gif;
		private ShowMaxImageView img;

		private LinearLayout ll_support;
		private LinearLayout ll_unsupport;
		private LinearLayout ll_comment;

		private ProgressBar progress;
		private CardView card;

		//用于处理多次点击造成的网络访问
		private boolean isClickFinish;

		public ViewHolder(View contentView) {
			super(contentView);

			isClickFinish = true;

			tv_author = (TextView) contentView.findViewById(R.id.tv_author);
			tv_content = (TextView) contentView.findViewById(R.id.tv_content);
			tv_time = (TextView) contentView.findViewById(R.id.tv_time);
			tv_like = (TextView) contentView.findViewById(R.id.tv_like);
			tv_unlike = (TextView) contentView.findViewById(R.id.tv_unlike);
			tv_comment_count = (TextView) contentView.findViewById(R.id.tv_comment_count);
			tv_unsupport_des = (TextView) contentView.findViewById(R.id.tv_unsupport_des);
			tv_support_des = (TextView) contentView.findViewById(R.id.tv_support_des);

			img_share = (ImageView) contentView.findViewById(R.id.img_share);
			img_gif = (ImageView) contentView.findViewById(R.id.img_gif);
			img = (ShowMaxImageView) contentView.findViewById(R.id.img);

			progress = (ProgressBar) contentView.findViewById(R.id.progress);
			card = (CardView) contentView.findViewById(R.id.card);

			ll_support = (LinearLayout) contentView.findViewById(R.id.ll_support);
			ll_unsupport = (LinearLayout) contentView.findViewById(R.id.ll_unsupport);
			ll_comment = (LinearLayout) contentView.findViewById(R.id.ll_comment);

		}
	}
}

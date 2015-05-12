package com.socks.jiandan.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.cache.FreshNewsCacheUtil;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.net.Request4FreshNews;
import com.socks.jiandan.ui.FreshNewsDetailActivity;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.view.AutoLoadRecyclerView;
import com.socks.jiandan.view.googleprogressbar.GoogleProgressBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 新鲜事碎片
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FreshNewsFragment extends BaseFragment {


	@InjectView(R.id.recycler_view)
	AutoLoadRecyclerView mRecyclerView;
	@InjectView(R.id.swipeRefreshLayout)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@InjectView(R.id.google_progress)
	GoogleProgressBar google_progress;

	private FreshNewsAdapter mAdapter;
	private LoadFinishCallBack mLoadFinisCallBack;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	//是否是大图模式
	private static boolean isLargeMode;

	public FreshNewsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mActionBar.setTitle("新鲜事");
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

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isLargeMode = sp.getBoolean(SettingFragment.ENABLE_FRESH_BIG, true);

		int loadingResource;

		//大图模式
		if (isLargeMode) {
			loadingResource = R.drawable.ic_loading_large;
		} else {
			loadingResource = R.drawable.ic_loading_small;
		}

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.showImageOnLoading(loadingResource)
				.build();

		mAdapter = new FreshNewsAdapter();
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.loadFirst();

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
		if (mRecyclerView != null && mAdapter.mFreshNews.size() > 0) {
			mRecyclerView.scrollToPosition(0);
		}
	}

	/**
	 * 新鲜事适配器
	 */
	public class FreshNewsAdapter extends RecyclerView.Adapter<ViewHolder> {

		private int page;
		private ArrayList<FreshNews> mFreshNews;
		private int lastPosition = -1;

		public FreshNewsAdapter() {
			mFreshNews = new ArrayList<FreshNews>();
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
			if (isLargeMode) {
				holder.card.clearAnimation();
			} else {
				holder.ll_content.clearAnimation();
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent,
		                                     int viewType) {
			int layoutId;

			if (isLargeMode) {
				layoutId = R.layout.item_fresh_news;
			} else {
				layoutId = R.layout.item_fresh_news_small;
			}

			View v = LayoutInflater.from(parent.getContext())
					.inflate(layoutId, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {

			final FreshNews freshNews = mFreshNews.get(position);
			imageLoader.displayImage(freshNews.getCustomFields().getThumb_m(), holder.img, options);
			holder.tv_title.setText(freshNews.getTitle());
			holder.tv_info.setText(freshNews.getAuthor().getName() + "@" + freshNews.getTags()
					.getTitle());

			holder.tv_views.setText("浏览" + freshNews.getCustomFields().getViews() + "次");

			if (isLargeMode) {
				holder.tv_share.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ShareUtil.shareText(getActivity(), freshNews.getTitle() + " " + freshNews.getUrl());
					}
				});

				holder.card.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), FreshNewsDetailActivity.class);
						intent.putExtra("FreshNews", mFreshNews);
						intent.putExtra("position", position);
						startActivity(intent);
					}
				});

				setAnimation(holder.card, position);
			} else {
				holder.ll_content.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), FreshNewsDetailActivity.class);
						intent.putExtra("FreshNews", mFreshNews);
						intent.putExtra("position", position);
						startActivity(intent);
					}
				});
				setAnimation(holder.ll_content, position);
			}

		}

		@Override
		public int getItemCount() {
			return mFreshNews.size();
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
				executeRequest(new Request4FreshNews(FreshNews.getUrlFreshNews(page),
						new Response.Listener<ArrayList<FreshNews>>() {
							@Override
							public void onResponse(ArrayList<FreshNews> response) {

								google_progress.setVisibility(View.GONE);
								mLoadFinisCallBack.loadFinish(null);
								if (mSwipeRefreshLayout.isRefreshing()) {
									mSwipeRefreshLayout.setRefreshing(false);
								}

								if (page == 1) {
									mAdapter.mFreshNews.clear();
									FreshNewsCacheUtil.getInstance(getActivity()).clearAllCache();
								}

								mAdapter.mFreshNews.addAll(response);
								notifyDataSetChanged();

								FreshNewsCacheUtil.getInstance(getActivity()).addResultCache(JSONParser.toString(response),
										page);

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
			} else {
				google_progress.setVisibility(View.GONE);
				mLoadFinisCallBack.loadFinish(null);
				if (mSwipeRefreshLayout.isRefreshing()) {
					mSwipeRefreshLayout.setRefreshing(false);
				}

				if (page == 1) {
					mFreshNews.clear();
					ShowToast.Short(ToastMsg.LOAD_NO_NETWORK);
				}

				mFreshNews.addAll(FreshNewsCacheUtil.getInstance(getActivity()).getCacheByPage(page));
				notifyDataSetChanged();
			}

		}

	}


	public static class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tv_title;
		private TextView tv_info;
		private TextView tv_views;
		private TextView tv_share;
		private ImageView img;
		private CardView card;

		private LinearLayout ll_content;

		public ViewHolder(View contentView) {
			super(contentView);
			tv_title = (TextView) contentView.findViewById(R.id.tv_title);
			tv_info = (TextView) contentView.findViewById(R.id.tv_info);
			tv_views = (TextView) contentView.findViewById(R.id.tv_views);
			img = (ImageView) contentView.findViewById(R.id.img);

			if (isLargeMode) {
				tv_share = (TextView) contentView.findViewById(R.id.tv_share);
				card = (CardView) contentView.findViewById(R.id.card);
			} else {
				ll_content = (LinearLayout) contentView.findViewById(R.id.ll_content);
			}

		}
	}


}

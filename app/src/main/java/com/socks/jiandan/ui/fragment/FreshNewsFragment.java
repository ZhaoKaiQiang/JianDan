package com.socks.jiandan.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.constant.ToastMsg;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.net.Request4FreshNews;
import com.socks.jiandan.ui.FreshNewsDetailActivity;
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
				.resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.ic_loading_small)
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
		if (mRecyclerView != null && mAdapter.freshNewses.size() > 0) {
			mRecyclerView.scrollToPosition(0);
		}
	}

	public class FreshNewsAdapter extends RecyclerView.Adapter<ViewHolder> {

		private int page;
		private ArrayList<FreshNews> freshNewses;

		public FreshNewsAdapter() {
			freshNewses = new ArrayList<FreshNews>();
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent,
		                                     int viewType) {
			View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_fresh_news, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {

			final FreshNews freshNews = freshNewses.get(position);
			imageLoader.displayImage(freshNews.getCustomFields().thumb_c, holder.img, options);
			holder.tv_title.setText(freshNews.getTitle());
			holder.tv_info.setText(freshNews.getAuthor().getName() + "@" + freshNews.getTags()
					.getTitle());

			holder.card_bg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), FreshNewsDetailActivity.class);
					intent.putExtra("FreshNews", freshNewses);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});


		}

		@Override
		public int getItemCount() {
			return freshNewses.size();
		}

		public void loadFirst() {
			page = 1;
			loadData();
		}

		public void loadNextPage() {
			page++;
			loadData();
		}

		private void loadData() {
			executeRequest(new Request4FreshNews(FreshNews.getUrlFreshNews(page),
					new Response.Listener<ArrayList<FreshNews>>() {
						@Override
						public void onResponse(ArrayList<FreshNews> response) {
							google_progress.setVisibility(View.GONE);

							if (page == 1) {
								mAdapter.freshNewses.clear();
								mAdapter.freshNewses.addAll(response);
							} else {
								mAdapter.freshNewses.addAll(response);
							}

							notifyDataSetChanged();

							if (mSwipeRefreshLayout.isRefreshing()) {
								mSwipeRefreshLayout.setRefreshing(false);
							}

							mLoadFinisCallBack.loadFinish(null);
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

	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tv_title;
		private TextView tv_info;
		private ImageView img;
		private CardView card_bg;

		public ViewHolder(View contentView) {
			super(contentView);
			tv_title = (TextView) contentView.findViewById(R.id.tv_title);
			tv_info = (TextView) contentView.findViewById(R.id.tv_info);
			img = (ImageView) contentView.findViewById(R.id.img);
			card_bg = (CardView) contentView.findViewById(R.id.card_bg);
		}
	}
}

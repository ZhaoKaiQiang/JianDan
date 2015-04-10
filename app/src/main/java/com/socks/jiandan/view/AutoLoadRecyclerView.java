package com.socks.jiandan.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.socks.jiandan.callback.LoadFinishCallBack;

/**
 * Created by zhaokaiqiang on 15/4/9.
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack{

	private onLoadMoreListener loadMoreListener;

	private LayoutManager mLayoutManager;

	private boolean isLoadingMore;

	public AutoLoadRecyclerView(Context context) {
		this(context, null);
	}

	public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		isLoadingMore = false;

		mLayoutManager = new LinearLayoutManager(context);
		setLayoutManager(mLayoutManager);

		setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
				int totalItemCount = mLayoutManager.getItemCount();
				//有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
				if (loadMoreListener != null &&!isLoadingMore && lastVisibleItem >= totalItemCount -
						2 && dy > 0) {
					loadMoreListener.loadMore();
					isLoadingMore = true;
				}
			}
		});

	}


	public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

	@Override
	public void loadFinish() {
		isLoadingMore = false;
	}


	public interface onLoadMoreListener {
		void loadMore();
	}


}

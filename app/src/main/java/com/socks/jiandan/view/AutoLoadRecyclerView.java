package com.socks.jiandan.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadMoreListener;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

/**
 * Created by zhaokaiqiang on 15/4/9.
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack {

    private LoadMoreListener loadMoreListener;
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
        addOnScrollListener(new AutoLoadScrollListener(null, true, true));
    }

    /**
     * 如果需要显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    public void setOnPauseListenerParams(boolean pauseOnScroll, boolean pauseOnFling) {
        addOnScrollListener(new AutoLoadScrollListener(ImageLoadProxy.getImageLoader(), pauseOnScroll, pauseOnFling));
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void loadFinish(Object obj) {
        isLoadingMore = false;
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，并且不是加载状态，并且剩下2个item，并且向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        2 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (imageLoader != null) {
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        imageLoader.resume();
                        break;
                    case SCROLL_STATE_DRAGGING:
                        if (pauseOnScroll) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;
                    case SCROLL_STATE_SETTLING:
                        if (pauseOnFling) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;
                }
            }
        }
    }

}

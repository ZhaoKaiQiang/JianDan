package com.socks.jiandan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.socks.jiandan.R;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.cache.FreshNewsCache;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.model.FreshNews;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.net.Request4FreshNews;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.ui.FreshNewsDetailActivity;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class FreshNewsAdapter extends RecyclerView.Adapter<FreshNewsAdapter.ViewHolder> {

    private int page;
    private int lastPosition = -1;
    private boolean isLargeMode;
    private Activity mActivity;
    private DisplayImageOptions options;
    private ArrayList<FreshNews> mFreshNews;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;

    public FreshNewsAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack, boolean isLargeMode) {
        this.mActivity = activity;
        this.isLargeMode = isLargeMode;
        this.mLoadFinisCallBack = loadFinisCallBack;
        this.mLoadResultCallBack = loadResultCallBack;
        mFreshNews = new ArrayList<>();

        int loadingResource = isLargeMode ? R.drawable.ic_loading_large : R.drawable.ic_loading_small;
        options = ImageLoadProxy.getOptions4PictureList(loadingResource);
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

        if (isLargeMode) {
            holder.card.clearAnimation();
        } else {
            holder.ll_content.clearAnimation();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = isLargeMode ? R.layout.item_fresh_news : R.layout.item_fresh_news_small;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final FreshNews freshNews = mFreshNews.get(position);

        ImageLoadProxy.displayImage(freshNews.getCustomFields().getThumb_m(), holder.img, options);
        holder.tv_title.setText(freshNews.getTitle());
        holder.tv_info.setText(freshNews.getAuthor().getName() + "@" + freshNews.getTags()
                .getTitle());
        holder.tv_views.setText("浏览" + freshNews.getCustomFields().getViews() + "次");

        if (isLargeMode) {
            holder.tv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtil.shareText(mActivity, freshNews.getTitle() + " " + freshNews.getUrl());
                }
            });

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(position);
                }
            });

            setAnimation(holder.card, position);
        } else {
            holder.ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(position);
                }
            });
            setAnimation(holder.ll_content, position);
        }

    }

    private void toDetailActivity(int position) {
        Intent intent = new Intent(mActivity, FreshNewsDetailActivity.class);
        intent.putExtra(FreshNewsDetailActivity.DATA_FRESH_NEWS, mFreshNews);
        intent.putExtra(FreshNewsDetailActivity.DATA_POSITION, position);
        mActivity.startActivity(intent);
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

        if (NetWorkUtil.isNetWorkConnected(mActivity)) {
            RequestManager.addRequest(new Request4FreshNews(FreshNews.getUrlFreshNews(page),
                    new Response.Listener<ArrayList<FreshNews>>() {
                        @Override
                        public void onResponse(ArrayList<FreshNews> response) {

                            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                            mLoadFinisCallBack.loadFinish(null);

                            if (page == 1) {
                                mFreshNews.clear();
                                FreshNewsCache.getInstance(mActivity).clearAllCache();
                            }

                            mFreshNews.addAll(response);
                            notifyDataSetChanged();

                            FreshNewsCache.getInstance(mActivity).addResultCache(JSONParser.toString(response),
                                    page);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
                    mLoadFinisCallBack.loadFinish(null);
                }
            }), mActivity);
        } else {
            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
            mLoadFinisCallBack.loadFinish(null);

            if (page == 1) {
                mFreshNews.clear();
                ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
            }

            mFreshNews.addAll(FreshNewsCache.getInstance(mActivity).getCacheByPage(page));
            notifyDataSetChanged();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.tv_info)
        TextView tv_info;
        @InjectView(R.id.tv_views)
        TextView tv_views;
        @Optional
        @InjectView(R.id.tv_share)
        TextView tv_share;
        @InjectView(R.id.img)
        ImageView img;
        @Optional
        @InjectView(R.id.card)
        CardView card;
        @Optional
        @InjectView(R.id.ll_content)
        LinearLayout ll_content;

        public ViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this, contentView);
        }
    }

}
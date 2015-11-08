package com.socks.jiandan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.cache.PictureCache;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.model.CommentNumber;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.net.Request4CommentCounts;
import com.socks.jiandan.net.Request4Picture;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.ui.CommentListActivity;
import com.socks.jiandan.ui.ImageDetailActivity;
import com.socks.jiandan.utils.FileUtil;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.TextUtil;
import com.socks.jiandan.view.ShowMaxImageView;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private int page;
    private int lastPosition = -1;
    private ArrayList<Picture> pictures;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;
    private Activity mActivity;
    private boolean isWifiConnected;
    private Picture.PictureType mType;
    private LoadFinishCallBack mSaveFileCallBack;

    public PictureAdapter(Activity activity, LoadResultCallBack loadResultCallBack, LoadFinishCallBack loadFinisCallBack, Picture.PictureType type) {
        mActivity = activity;
        mType = type;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        pictures = new ArrayList<>();
        isWifiConnected = NetWorkUtil.isWifiConnected(mActivity);
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
    public void onViewDetachedFromWindow(PictureViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pic, parent, false);
        return new PictureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PictureViewHolder holder, final int position) {

        final Picture picture = pictures.get(position);

        String picUrl = picture.getPics()[0];

        if (picUrl.endsWith(".gif")) {
            holder.img_gif.setVisibility(View.VISIBLE);
            //非WIFI网络情况下，GIF图只加载缩略图，详情页才加载真实图片
            if (!isWifiConnected) {
                picUrl = picUrl.replace("mw600", "small").replace("mw1200", "small").replace
                        ("large", "small");
            }
        } else {
            holder.img_gif.setVisibility(View.GONE);
        }

        holder.progress.setProgress(0);
        holder.progress.setVisibility(View.VISIBLE);

        ImageLoadProxy.displayImageList(picUrl, holder.img, R.drawable.ic_loading_large, new
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
                Intent intent = new Intent(mActivity, ImageDetailActivity.class);

                intent.putExtra(BaseActivity.DATA_IMAGE_AUTHOR, picture.getComment_author());
                intent.putExtra(BaseActivity.DATA_IMAGE_URL, picture.getPics());
                intent.putExtra(BaseActivity.DATA_IMAGE_ID, picture.getComment_ID());
                intent.putExtra(BaseActivity.DATA_THREAD_KEY, "comment-" + picture.getComment_ID());

                if (picture.getPics()[0].endsWith(".gif")) {
                    intent.putExtra(BaseActivity.DATA_IS_NEED_WEBVIEW, true);
                }

                mActivity.startActivity(intent);
            }
        });

        holder.tv_author.setText(picture.getComment_author());
        holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(picture.getComment_date()));
        holder.tv_like.setText(picture.getVote_positive());
        holder.tv_comment_count.setText(picture.getComment_counts());

        //用于恢复默认的文字
        holder.tv_like.setTypeface(Typeface.DEFAULT);
        holder.tv_like.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));

        holder.tv_unlike.setText(picture.getVote_negative());
        holder.tv_unlike.setTypeface(Typeface.DEFAULT);
        holder.tv_unlike.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_un_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mActivity)
                        .items(R.array.picture_dialog)
                        .backgroundColor(mActivity.getResources().getColor(JDApplication.COLOR_OF_DIALOG))
                        .contentColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                switch (which) {
                                    //分享
                                    case 0:
                                        ShareUtil.sharePicture(mActivity, picture
                                                .getPics()[0]);
                                        break;
                                    //保存
                                    case 1:
                                        FileUtil.savePicture(mActivity, picture
                                                .getPics()[0],mSaveFileCallBack);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommentListActivity.class);
                intent.putExtra(BaseActivity.DATA_THREAD_KEY, "comment-" + picture.getComment_ID());
                mActivity.startActivity(intent);
            }
        });

        setAnimation(holder.card, position);

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

        if (NetWorkUtil.isNetWorkConnected(mActivity)) {
            loadData();
        } else {
            loadCache();
        }

    }

    private void loadData() {

        RequestManager.addRequest(new Request4Picture(Picture.getRequestUrl(mType, page),
                new Response.Listener<ArrayList<Picture>>
                        () {
                    @Override
                    public void onResponse(ArrayList<Picture> response) {
                        getCommentCounts(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
                mLoadFinisCallBack.loadFinish(null);
            }
        }), mActivity);
    }

    private void loadCache() {

        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
        mLoadFinisCallBack.loadFinish(null);
        PictureCache pictureCacheUtil = PictureCache.getInstance(mActivity);
        if (page == 1) {
            pictures.clear();
            ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
        }
        pictures.addAll(pictureCacheUtil.getCacheByPage(page));
        notifyDataSetChanged();

    }

    private void getCommentCounts(final ArrayList<Picture> pictures) {

        StringBuilder sb = new StringBuilder();
        for (Picture joke : pictures) {
            sb.append("comment-" + joke.getComment_ID() + ",");
        }

        RequestManager.addRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(sb.toString()), new Response
                .Listener<ArrayList<CommentNumber>>() {

            @Override
            public void onResponse(ArrayList<CommentNumber> response) {

                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                mLoadFinisCallBack.loadFinish(null);

                for (int i = 0; i < pictures.size(); i++) {
                    pictures.get(i).setComment_counts(response.get(i).getComments() + "");
                }
                if (page == 1) {
                    PictureAdapter.this.pictures.clear();
                    PictureCache.getInstance(mActivity).clearAllCache();
                }
                PictureAdapter.this.pictures.addAll(pictures);
                notifyDataSetChanged();
                //加载完毕后缓存
                PictureCache.getInstance(mActivity).addResultCache(JSONParser.toString
                        (pictures), page);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowToast.Short(ConstantString.LOAD_FAILED);
                mLoadFinisCallBack.loadFinish(null);
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
            }
        }
        ), mActivity);
    }

    public void setIsWifi(boolean isWifiConnected) {
        this.isWifiConnected = isWifiConnected;
    }

    public void setmSaveFileCallBack(LoadFinishCallBack mSaveFileCallBack) {
        this.mSaveFileCallBack = mSaveFileCallBack;
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_author)
        TextView tv_author;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.tv_like)
        TextView tv_like;
        @InjectView(R.id.tv_unlike)
        TextView tv_unlike;
        @InjectView(R.id.tv_comment_count)
        TextView tv_comment_count;
        @InjectView(R.id.tv_unsupport_des)
        TextView tv_un_support_des;
        @InjectView(R.id.tv_support_des)
        TextView tv_support_des;

        @InjectView(R.id.img_share)
        ImageView img_share;
        @InjectView(R.id.img_gif)
        ImageView img_gif;
        @InjectView(R.id.img)
        ShowMaxImageView img;

        @InjectView(R.id.ll_comment)
        LinearLayout ll_comment;
        @InjectView(R.id.progress)
        ProgressBar progress;
        @InjectView(R.id.card)
        CardView card;

        public PictureViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this, contentView);
        }
    }
}
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.cache.JokeCache;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.model.CommentNumber;
import com.socks.jiandan.model.Joke;
import com.socks.jiandan.net.JSONParser;
import com.socks.jiandan.net.Request4CommentCounts;
import com.socks.jiandan.net.Request4Joke;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.ui.CommentListActivity;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShareUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.TextUtil;

import java.util.ArrayList;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeViewHolder> {

    private int page;
    private int lastPosition = -1;
    private ArrayList<Joke> mJokes;
    private Activity mActivity;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinisCallBack;

    public JokeAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        mJokes = new ArrayList<>();
    }

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(JokeViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_joke, parent, false);
        return new JokeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final JokeViewHolder holder, final int position) {

        final Joke joke = mJokes.get(position);
        holder.tv_content.setText(joke.getComment_content());
        holder.tv_author.setText(joke.getComment_author());
        holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(joke.getComment_date()));
        holder.tv_like.setText(joke.getVote_positive());
        holder.tv_comment_count.setText(joke.getComment_counts());
        holder.tv_unlike.setText(joke.getVote_negative());

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mActivity)
                        .items(R.array.joke_dialog)
                        .backgroundColor(mActivity.getResources().getColor(JDApplication.COLOR_OF_DIALOG))
                        .contentColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                switch (which) {
                                    //分享
                                    case 0:
                                        ShareUtil.shareText(mActivity, joke.getComment_content().trim());
                                        break;
                                    //复制
                                    case 1:
                                        TextUtil.copy(mActivity, joke.getComment_content());
                                        break;
                                }

                            }
                        }).show();
            }
        });

        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommentListActivity.class);
                intent.putExtra("thread_key", "comment-" + joke.getComment_ID());
                mActivity.startActivity(intent);
            }
        });

        setAnimation(holder.card, position);

    }

    @Override
    public int getItemCount() {
        return mJokes.size();
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
        RequestManager.addRequest(new Request4Joke(Joke.getRequestUrl(page),
                new Response.Listener<ArrayList<Joke>>
                        () {
                    @Override
                    public void onResponse(ArrayList<Joke> response) {
                        getCommentCounts(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinisCallBack.loadFinish(null);
            }
        }), mActivity);
    }

    private void loadCache() {
        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
        JokeCache jokeCacheUtil = JokeCache.getInstance(mActivity);
        if (page == 1) {
            mJokes.clear();
            ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
        }
        mJokes.addAll(jokeCacheUtil.getCacheByPage(page));
        notifyDataSetChanged();
    }

    private void getCommentCounts(final ArrayList<Joke> jokes) {

        StringBuilder sb = new StringBuilder();
        for (Joke joke : jokes) {
            sb.append("comment-" + joke.getComment_ID() + ",");
        }

        String url = sb.toString();
        if (url.endsWith(",")) {
            url = url.substring(0, url.length() - 1);
        }

        RequestManager.addRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(url), new Response
                .Listener<ArrayList<CommentNumber>>() {

            @Override
            public void onResponse(ArrayList<CommentNumber> response) {

                for (int i = 0; i < jokes.size(); i++) {
                    jokes.get(i).setComment_counts(response.get(i).getComments() + "");
                }

                if (page == 1) {
                    mJokes.clear();
                    //首次正常加载之后，清空之前的缓存
                    JokeCache.getInstance(mActivity).clearAllCache();
                }

                mJokes.addAll(jokes);
                notifyDataSetChanged();

                //加载完毕后缓存
                JokeCache.getInstance(mActivity).addResultCache(JSONParser.toString(jokes),
                        page);
                mLoadFinisCallBack.loadFinish(null);
                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
                mLoadFinisCallBack.loadFinish(null);
            }
        }
        ), mActivity);

    }

    public static class JokeViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_author;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_like;
        private TextView tv_unlike;
        private TextView tv_comment_count;

        private ImageView img_share;
        private CardView card;
        private LinearLayout ll_comment;

        public JokeViewHolder(View contentView) {
            super(contentView);

            tv_author = (TextView) contentView.findViewById(R.id.tv_author);
            tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            tv_like = (TextView) contentView.findViewById(R.id.tv_like);
            tv_unlike = (TextView) contentView.findViewById(R.id.tv_unlike);
            tv_comment_count = (TextView) contentView.findViewById(R.id.tv_comment_count);

            img_share = (ImageView) contentView.findViewById(R.id.img_share);
            card = (CardView) contentView.findViewById(R.id.card);
            ll_comment = (LinearLayout) contentView.findViewById(R.id.ll_comment);
        }
    }

}


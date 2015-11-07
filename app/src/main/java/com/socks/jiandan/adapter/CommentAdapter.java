package com.socks.jiandan.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.socks.jiandan.R;
import com.socks.jiandan.base.ConstantString;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.model.Comment4FreshNews;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.model.Vote;
import com.socks.jiandan.net.Request4CommentList;
import com.socks.jiandan.net.Request4FreshNewsCommentList;
import com.socks.jiandan.net.Request4Vote;
import com.socks.jiandan.net.RequestManager;
import com.socks.jiandan.ui.PushCommentActivity;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.utils.String2TimeUtil;
import com.socks.jiandan.utils.logger.Logger;
import com.socks.jiandan.view.floorview.FloorView;
import com.socks.jiandan.view.floorview.SubComments;
import com.socks.jiandan.view.floorview.SubFloorFactory;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Commentator> commentators;
    private ArrayList<Comment4FreshNews> commentators4FreshNews;

    private Activity mActivity;
    private String thread_key;
    private String thread_id;
    private LoadResultCallBack mLoadResultCallBack;
    private boolean isFromFreshNews;

    public CommentAdapter(Activity activity, String thread_key, boolean isFromFreshNews, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        this.thread_key = thread_key;
        this.isFromFreshNews = isFromFreshNews;
        mLoadResultCallBack = loadResultCallBack;
        if (isFromFreshNews) {
            commentators4FreshNews = new ArrayList<>();
        } else {
            commentators = new ArrayList<>();
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case Commentator.TYPE_HOT:
            case Commentator.TYPE_NEW:
                return new CommentViewHolder(mActivity.getLayoutInflater().inflate(R.layout
                        .item_comment_flag, parent, false));
            case Commentator.TYPE_NORMAL:
                return new CommentViewHolder(mActivity.getLayoutInflater().inflate(R.layout.item_comment, parent,
                        false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

        Commentator commentator;
        if (isFromFreshNews) {
            commentator = commentators4FreshNews.get(position);
        } else {
            commentator = commentators.get(position);
        }

        switch (commentator.getType()) {
            case Commentator.TYPE_HOT:
                holder.tv_flag.setText("热门评论");
                break;
            case Commentator.TYPE_NEW:
                holder.tv_flag.setText("最新评论");
                break;
            case Commentator.TYPE_NORMAL:
                final Commentator comment = commentator;
                holder.tv_name.setText(commentator.getName());
                holder.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(mActivity)
                                .title(comment.getName())
                                .items(R.array.comment_dialog)
                                .backgroundColor(mActivity.getResources().getColor(R.color.primary))
                                .contentColor(Color.WHITE)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                        switch (which) {
                                            //评论
                                            case 0:
                                                Intent intent = new Intent
                                                        (mActivity, PushCommentActivity.class);
                                                intent.putExtra("parent_id", comment.getPost_id());
                                                intent.putExtra("thread_id", thread_id);
                                                intent.putExtra("parent_name", comment
                                                        .getName());
                                                mActivity.startActivityForResult(intent, 0);
                                                break;
                                            case 1:
                                                //复制到剪贴板
                                                ClipboardManager clip = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                                clip.setPrimaryClip(ClipData.newPlainText
                                                        (null, comment.getMessage()));
                                                ShowToast.Short(ConstantString.COPY_SUCCESS);
                                                break;
                                        }
                                    }
                                }).show();
                    }
                });

                if (isFromFreshNews) {
                    holder.ll_vote.setVisibility(View.VISIBLE);
                    Comment4FreshNews commentators4FreshNews = (Comment4FreshNews) commentator;
                    holder.ll_support.setOnClickListener(new onVoteClickListener(commentators4FreshNews.getId() + "",
                            Vote.OO, holder, commentators4FreshNews));
                    holder.ll_un_support.setOnClickListener(new onVoteClickListener(commentators4FreshNews.getId() + "",
                            Vote.XX, holder, commentators4FreshNews));
                    holder.tv_content.setText(commentators4FreshNews.getCommentContent());
                    ImageLoadProxy.displayHeadIcon(commentators4FreshNews.getAvatar_url(), holder.img_header);
                } else {
                    holder.ll_vote.setVisibility(View.GONE);
                    String timeString = commentator.getCreated_at().replace("T", " ");
                    timeString = timeString.substring(0, timeString.indexOf("+"));
                    holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(timeString));
                    holder.tv_content.setText(commentator.getMessage());
                    ImageLoadProxy.displayHeadIcon(commentator.getAvatar_url(), holder.img_header);
                }

                //有楼层,盖楼
                if (commentator.getFloorNum() > 1) {
                    SubComments subComments;
                    if (isFromFreshNews) {
                        subComments = new SubComments(addFloors4FreshNews((Comment4FreshNews) commentator));
                    } else {
                        subComments = new SubComments(addFloors(commentator));
                    }

                    holder.floors_parent.setComments(subComments);
                    holder.floors_parent.setFactory(new SubFloorFactory());
                    holder.floors_parent.setBoundDrawer(mActivity.getResources().getDrawable(
                            R.drawable.bg_comment));
                    holder.floors_parent.init();
                } else {
                    holder.floors_parent.setVisibility(View.GONE);
                }
                break;
        }

    }

    private class onVoteClickListener implements View.OnClickListener {

        private String comment_ID;
        private String style;
        private CommentViewHolder holder;
        private Comment4FreshNews commentator;

        public onVoteClickListener(String comment_ID, String style, CommentViewHolder holder, Comment4FreshNews commentator) {
            this.comment_ID = comment_ID;
            this.style = style;
            this.holder = holder;
            this.commentator = commentator;
        }

        @Override
        public void onClick(View v) {
            if (holder.isClickFinish) {
                vote(comment_ID, style, holder, commentator);
                holder.isClickFinish = false;
            }
        }
    }

    public void vote(String comment_ID, String style, final CommentViewHolder holder, final Comment4FreshNews commentator) {

        String url;

        if (style.equals(Vote.XX)) {
            url = Vote.getXXUrl(comment_ID);
        } else {
            url = Vote.getOOUrl(comment_ID);
        }

        RequestManager.addRequest(new Request4Vote(url, new
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
                            holder.tv_like.setTextColor(mActivity.getResources().getColor
                                    (android.R.color.holo_red_light));
                            holder.tv_support_des.setTextColor(mActivity.getResources().getColor
                                    (android.R.color.holo_red_light));

                        } else if (result.equals(Vote.RESULT_XX_SUCCESS)) {
                            ShowToast.Short("疼...轻点插");
                            //变绿+1
                            int vote = commentator.getVote_negative();
                            commentator.setVote_negative(vote + 1);
                            holder.tv_unlike.setText(commentator.getVote_negative() + "");
                            holder.tv_unlike.setTypeface(Typeface.DEFAULT_BOLD);
                            holder.tv_unlike.setTextColor(mActivity.getResources().getColor
                                    (android.R.color.holo_green_light));
                            holder.tv_un_support_des.setTextColor(mActivity.getResources().getColor
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
                ShowToast.Short(ConstantString.VOTE_FAILED);
                holder.isClickFinish = true;
            }
        }), mActivity);
    }

    private List<Comment4FreshNews> addFloors4FreshNews(Comment4FreshNews commentator) {
        return commentator.getParentComments();
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

        if (isFromFreshNews) {
            return commentators4FreshNews.size();
        } else {
            return commentators.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFromFreshNews) {
            return commentators4FreshNews.get(position).getType();
        } else {
            return commentators.get(position).getType();
        }
    }

    public void loadData() {
        RequestManager.addRequest(new Request4CommentList(Commentator.getUrlCommentList(thread_key), new Response
                .Listener<ArrayList<Commentator>>() {
            @Override
            public void onResponse(ArrayList<Commentator> response) {

                Logger.d("size = " + response.size());

                if (response.size() == 0) {
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_NONE, null);
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

                    notifyDataSetChanged();
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
            }
        }, new LoadFinishCallBack() {
            @Override
            public void loadFinish(Object obj) {
                thread_id = (String) obj;
            }
        }), mActivity);
    }


    public void loadData4FreshNews() {
        RequestManager.addRequest(new Request4FreshNewsCommentList(Comment4FreshNews.getUrlComments(thread_key), new Response
                .Listener<ArrayList<Comment4FreshNews>>() {
            @Override
            public void onResponse(ArrayList<Comment4FreshNews> response) {

                if (response.size() == 0) {
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_NONE, null);
                } else {
                    commentators4FreshNews.clear();

                    //如果评论条数大于6，就选择positive前6作为热门评论
                    if (response.size() > 6) {
                        Comment4FreshNews comment4FreshNews = new Comment4FreshNews();
                        comment4FreshNews.setType(Comment4FreshNews.TYPE_HOT);
                        commentators4FreshNews.add(comment4FreshNews);

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

                        commentators4FreshNews.addAll(subComments);

                    }

                    Comment4FreshNews comment4FreshNews = new Comment4FreshNews();
                    comment4FreshNews.setType(Comment4FreshNews.TYPE_NEW);
                    commentators4FreshNews.add(comment4FreshNews);

                    Collections.sort(response);

                    for (Comment4FreshNews comment4Normal : response) {
                        if (comment4Normal.getTag().equals(Comment4FreshNews.TAG_NORMAL)) {
                            commentators4FreshNews.add(comment4Normal);
                        }
                    }

                    notifyDataSetChanged();
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
            }
        }, new LoadFinishCallBack() {
            @Override
            public void loadFinish(Object obj) {
                thread_id = (String) obj;
            }
        }), mActivity);
    }

    public String getThreadId() {
        return thread_id;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        @Optional
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @Optional
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @Optional
        @InjectView(R.id.tv_flag)
        TextView tv_flag;
        @Optional
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @Optional
        @InjectView(R.id.ll_vote)
        LinearLayout ll_vote;
        @Optional
        @InjectView(R.id.ll_support)
        LinearLayout ll_support;
        @Optional
        @InjectView(R.id.ll_unsupport)
        LinearLayout ll_un_support;
        @Optional
        @InjectView(R.id.img_header)
        ImageView img_header;
        @Optional
        @InjectView(R.id.floors_parent)
        FloorView floors_parent;
        @Optional
        @InjectView(R.id.tv_like)
        TextView tv_like;
        @Optional
        @InjectView(R.id.tv_support_des)
        TextView tv_support_des;
        @Optional
        @InjectView(R.id.tv_unlike)
        TextView tv_unlike;
        @Optional
        @InjectView(R.id.tv_unsupport_des)
        TextView tv_un_support_des;

        //用于处理多次点击造成的网络访问
        private boolean isClickFinish;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            setIsRecyclable(false);
        }
    }
}
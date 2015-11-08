package com.socks.jiandan.model;

public class CommentNumber {

    //用于获取评论数量
    public static final String URL_COMMENT_COUNTS = "http://jandan.duoshuo.com/api/threads/counts.json?threads=";
    public static final String COMMENTS = "comments";

    private int comments;

    public CommentNumber() {
    }

    public CommentNumber(int comments) {
        this.comments = comments;
    }

    public static String getCommentCountsURL(String params) {
        return URL_COMMENT_COUNTS + params;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

}

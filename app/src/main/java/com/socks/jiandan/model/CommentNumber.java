package com.socks.jiandan.model;

public class CommentNumber {

    //用于获取评论数量
    public static final String URL_COMMENT_COUNTS = "http://jandan.duoshuo.com/api/threads/counts.json?threads=";
    public static final String COMMENTS = "comments";

    public static final String THREAD_ID = "thread_id";
    public static final String THREAD_KEY = "thread_key";

    private int comments;
    private String thread_id;
    private String thread_key;

    public CommentNumber() {
    }

    public CommentNumber(String thread_id, String thread_key, int comments) {
        this.thread_id = thread_id;
        this.thread_key = thread_key;
        this.comments = comments;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_key() {
        return thread_key;
    }

    public void setThread_key(String thread_key) {
        this.thread_key = thread_key;
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

package com.socks.jiandan.model;

import java.io.Serializable;

public class Joke implements Serializable {

    public static final String URL_JOKE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan" +
            ".get_duan_comments&page=";

    private String comment_ID;
    private String comment_post_ID;
    private String comment_author;
    private String comment_author_email;
    private String comment_author_url;
    private String comment_author_IP;
    private String comment_date;
    private String comment_date_gmt;
    private String comment_content;
    private String text_content;
    private String comment_agent;
    private String vote_positive;
    private String vote_negative;
    //评论数量，需要单独获取
    private String comment_counts;

    public Joke() {
    }

    public static String getRequestUrl(int page) {
        return URL_JOKE + page;
    }

    public String getComment_ID() {
        return comment_ID;
    }

    public String getComment_author() {
        return comment_author;
    }

    public String getComment_date() {
        return comment_date;
    }

    public String getComment_content() {
        return comment_content;
    }

    public String getVote_positive() {
        return vote_positive;
    }

    public void setVote_positive(String vote_positive) {
        this.vote_positive = vote_positive;
    }

    public String getVote_negative() {
        return vote_negative;
    }

    public void setVote_negative(String vote_negative) {
        this.vote_negative = vote_negative;
    }

    public String getComment_counts() {
        return comment_counts;
    }

    public void setComment_counts(String comment_counts) {
        this.comment_counts = comment_counts;
    }

}

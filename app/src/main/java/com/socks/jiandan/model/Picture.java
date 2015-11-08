package com.socks.jiandan.model;

import java.io.Serializable;

public class Picture implements Serializable {

    public static final String URL_BORING_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page=";
    public static final String URL_SISTER = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";

    public enum PictureType {
        BoringPicture, Sister
    }

    private String comment_ID;
    private String comment_author;
    private String comment_date;
    private String text_content;
    private String vote_positive;
    private String vote_negative;
    private String[] pics;
    //评论数量，需要单独获取
    private String comment_counts;

    public Picture() {
    }

    public static String getRequestUrl(PictureType type, int page) {

        switch (type) {
            case BoringPicture:
                return URL_BORING_PICTURE + page;
            case Sister:
                return URL_SISTER + page;
            default:
                return "";
        }
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

    public String getText_content() {
        return text_content;
    }

    public String getVote_positive() {
        return vote_positive;
    }

    public String getVote_negative() {
        return vote_negative;
    }

    public String getComment_counts() {
        return comment_counts;
    }

    public void setComment_counts(String comment_counts) {
        this.comment_counts = comment_counts;
    }

    public String[] getPics() {
        return pics;
    }

}

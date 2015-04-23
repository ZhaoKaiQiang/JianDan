package com.socks.jiandan.model;

import java.io.Serializable;

/**
 * Created by zhaokaiqiang on 15/4/8.
 */
public class Picture implements Serializable {

	public static final String URL_DUANZI = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page=";
	public static final String URL_SYSTER = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";

	public enum PictureType {
		Duanzi, Syster;
	}


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
	private String[] pics;
	//评论数量，需要单独获取
	private String comment_counts;

	public Picture() {
	}

	public Picture(String comment_ID, String comment_post_ID,
	               String comment_author, String comment_author_email,
	               String comment_author_url, String comment_author_IP,
	               String comment_date, String comment_date_gmt,
	               String comment_content, String text_content, String comment_agent,
	               String vote_positive, String vote_negative, String comment_counts, String[]
			               pics) {
		super();
		this.comment_ID = comment_ID;
		this.comment_post_ID = comment_post_ID;
		this.comment_author = comment_author;
		this.comment_author_email = comment_author_email;
		this.comment_author_url = comment_author_url;
		this.comment_author_IP = comment_author_IP;
		this.comment_date = comment_date;
		this.comment_date_gmt = comment_date_gmt;
		this.comment_content = comment_content;
		this.text_content = text_content;
		this.comment_agent = comment_agent;
		this.vote_positive = vote_positive;
		this.vote_negative = vote_negative;
		this.comment_counts = comment_counts;
		this.pics = pics;
	}

	public static String getRequestUrl(PictureType type, int page) {

		switch (type) {
			case Duanzi:
				return URL_DUANZI + page;
			case Syster:
				return URL_SYSTER + page;
			default:
				return "";
		}
	}

	public String getComment_ID() {
		return comment_ID;
	}

	public void setComment_ID(String comment_ID) {
		this.comment_ID = comment_ID;
	}

	public String getComment_post_ID() {
		return comment_post_ID;
	}

	public void setComment_post_ID(String comment_post_ID) {
		this.comment_post_ID = comment_post_ID;
	}

	public String getComment_author() {
		return comment_author;
	}

	public void setComment_author(String comment_author) {
		this.comment_author = comment_author;
	}

	public String getComment_author_email() {
		return comment_author_email;
	}

	public void setComment_author_email(String comment_author_email) {
		this.comment_author_email = comment_author_email;
	}

	public String getComment_author_url() {
		return comment_author_url;
	}

	public void setComment_author_url(String comment_author_url) {
		this.comment_author_url = comment_author_url;
	}

	public String getComment_author_IP() {
		return comment_author_IP;
	}

	public void setComment_author_IP(String comment_author_IP) {
		this.comment_author_IP = comment_author_IP;
	}

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public String getComment_date_gmt() {
		return comment_date_gmt;
	}

	public void setComment_date_gmt(String comment_date_gmt) {
		this.comment_date_gmt = comment_date_gmt;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getText_content() {
		return text_content;
	}

	public void setText_content(String text_content) {
		this.text_content = text_content;
	}

	public String getComment_agent() {
		return comment_agent;
	}

	public void setComment_agent(String comment_agent) {
		this.comment_agent = comment_agent;
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

	public String[] getPics() {
		return pics;
	}

	public void setPics(String[] pics) {
		this.pics = pics;
	}

	@Override
	public String toString() {
		return "Picture{" +
				"comment_ID='" + comment_ID + '\'' +
				", comment_post_ID='" + comment_post_ID + '\'' +
				", comment_author='" + comment_author + '\'' +
				", comment_author_email='" + comment_author_email + '\'' +
				", comment_author_url='" + comment_author_url + '\'' +
				", comment_author_IP='" + comment_author_IP + '\'' +
				", comment_date='" + comment_date + '\'' +
				", comment_date_gmt='" + comment_date_gmt + '\'' +
				", comment_content='" + comment_content + '\'' +
				", text_content='" + text_content + '\'' +
				", comment_agent='" + comment_agent + '\'' +
				", vote_positive='" + vote_positive + '\'' +
				", vote_negative='" + vote_negative + '\'' +
				", pics=" + pics +
				", comment_counts='" + comment_counts + '\'' +
				'}';
	}
}

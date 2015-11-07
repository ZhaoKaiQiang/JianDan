package com.socks.jiandan.model;

/**
 * Created by zhaokaiqiang on 15/4/28.
 */
public class Video {

	public static final String URL_VIDEOS = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_video_comments&page=";

	private String title;
	private String video_source;
	private String url;
	private String desc;
	private String imgUrl;
	private String imgUrl4Big;

	private String comment_ID;
	private String vote_positive;
	private String vote_negative;
	private String comment_count;

	public Video() {
	}

	public static String getUrlVideos(int page) {
		return URL_VIDEOS + page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideo_source() {
		return video_source;
	}

	public void setVideo_source(String video_source) {
		this.video_source = video_source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl4Big() {
		return imgUrl4Big;
	}

	public void setImgUrl4Big(String imgUrl4Big) {
		this.imgUrl4Big = imgUrl4Big;
	}

	public String getComment_ID() {
		return comment_ID;
	}

	public void setComment_ID(String comment_ID) {
		this.comment_ID = comment_ID;
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

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	@Override
	public String toString() {
		return "Video{" +
				"title='" + title + '\'' +
				", video_source='" + video_source + '\'' +
				", url='" + url + '\'' +
				", desc='" + desc + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", imgUrl4Big='" + imgUrl4Big + '\'' +
				", comment_ID='" + comment_ID + '\'' +
				", vote_positive='" + vote_positive + '\'' +
				", vote_negative='" + vote_negative + '\'' +
				", comment_count='" + comment_count + '\'' +
				'}';
	}
}

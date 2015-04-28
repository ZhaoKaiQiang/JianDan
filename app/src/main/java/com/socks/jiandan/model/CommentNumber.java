package com.socks.jiandan.model;

/**
 * Created by zhaokaiqiang on 15/4/10.
 */
public class CommentNumber {

	//用于获取评论数量
	public static final String URL_COMMENT_COUNTS = "http://jandan.duoshuo.com/api/threads/counts.json?threads=";

	public static final String THREAD_ID="thread_id";
	public static final String THREAD_KEY="thread_key";
	public static final String COMMENTS="comments";

	private String thread_id;
	private String thread_key;
	private int comments;

	public CommentNumber() {
	}

	public CommentNumber(String thread_id, String thread_key, int comments) {
		this.thread_id = thread_id;
		this.thread_key = thread_key;
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

	@Override
	public String toString() {
		return "Comment{" +
				"thread_id='" + thread_id + '\'' +
				", thread_key='" + thread_key + '\'' +
				", comments=" + comments +
				'}';
	}
}

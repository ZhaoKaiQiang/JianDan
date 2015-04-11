package com.socks.jiandan.model;

/**
 * Created by zhaokaiqiang on 15/4/10.
 */
public class Commentator {

	public static final String URL_COMMENT_LIST = "http://jandan.duoshuo.com/api/threads/listPosts.json?thread_key=";

	private String avatar_url;
	private String created_at;
	private String name;
	private String message;

	public Commentator() {
	}

	public Commentator(String avatar_url, String created_at, String name, String message) {
		this.avatar_url = avatar_url;
		this.created_at = created_at;
		this.name = name;
		this.message = message;
	}

	/**
	 * 获取评论列表详细数据的URL
	 * @param thread_id
	 * @return
	 */
	public static String getUrlCommentList(String thread_id) {
		return URL_COMMENT_LIST + thread_id;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Commentator{" +
				"avatar_url='" + avatar_url + '\'' +
				", created_at='" + created_at + '\'' +
				", name='" + name + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}

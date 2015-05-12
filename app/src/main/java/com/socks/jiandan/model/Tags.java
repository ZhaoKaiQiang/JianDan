package com.socks.jiandan.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 新鲜事中的自定义字段
 * Created by zhaokaiqiang on 15/4/24.
 */
public class Tags implements Serializable {

	private int id;
	private String slug;
	private String title;
	private String description;
	private int post_count;

	public static Tags parse(final JSONArray jsonArray) {
		Tags tags;
		if (jsonArray == null) {
			tags = null;
		} else {
			tags = new Tags();
			JSONObject optJSONObject = jsonArray.optJSONObject(0);
			if (optJSONObject != null) {
				tags.id = optJSONObject.optInt("id");
				tags.slug = optJSONObject.optString("slug");
				tags.title = optJSONObject.optString("title");
				tags.description = optJSONObject.optString("description");
				tags.post_count = optJSONObject.optInt("post_count");
			}
		}
		return tags;
	}

	public static Tags parseCache(final JSONObject jsonObject) {
		Tags tags;
		if (jsonObject == null) {
			tags = null;
		} else {
			tags = new Tags();
			tags.id = jsonObject.optInt("id");
			tags.slug = jsonObject.optString("slug");
			tags.title = jsonObject.optString("title");
			tags.description = jsonObject.optString("description");
			tags.post_count = jsonObject.optInt("post_count");
		}
		return tags;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPost_count() {
		return post_count;
	}

	public void setPost_count(int post_count) {
		this.post_count = post_count;
	}
}

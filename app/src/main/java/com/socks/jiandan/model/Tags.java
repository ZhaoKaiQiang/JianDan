package com.socks.jiandan.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 新鲜事中的自定义字段
 */
public class Tags implements Serializable {

    private int id;
    private String title;
    private String description;

    public static Tags parse(final JSONArray jsonArray) {
        Tags tags;
        if (jsonArray == null) {
            tags = null;
        } else {
            tags = new Tags();
            JSONObject optJSONObject = jsonArray.optJSONObject(0);
            if (optJSONObject != null) {
                tags.id = optJSONObject.optInt("id");
                tags.title = optJSONObject.optString("title");
                tags.description = optJSONObject.optString("description");
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
            tags.title = jsonObject.optString("title");
            tags.description = jsonObject.optString("description");
        }
        return tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}

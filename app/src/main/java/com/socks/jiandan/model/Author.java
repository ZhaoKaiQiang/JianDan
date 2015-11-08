package com.socks.jiandan.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Author implements Serializable {

    public String id;
    public String slug;
    public String name;
    public String first_name;
    public String last_name;
    public String nickname;
    public String url;
    public String description;

    public static Author parse(final JSONObject jsonObject) {
        Author author;
        if (jsonObject == null) {
            author = null;
        } else {
            author = new Author();
            author.id = jsonObject.optString("id");
            author.slug = jsonObject.optString("slug");
            author.name = jsonObject.optString("name");
            author.first_name = jsonObject.optString("first_name");
            author.last_name = jsonObject.optString("last_name");
            author.nickname = jsonObject.optString("nickname");
            author.url = jsonObject.optString("url");
            author.description = jsonObject.optString("description");
        }
        return author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

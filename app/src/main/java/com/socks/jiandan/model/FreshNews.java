package com.socks.jiandan.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 新鲜事
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FreshNews implements Serializable {

    public static final String URL_FRESH_NEWS = "http://jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1&page=";

    public static final String URL_FRESH_NEWS_DETAIL = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&include=content&id=";

    //文章id
    private String id;
    //文章标题
    private String title;
    //文章地址
    private String url;
    //发布日期
    private String date;
    //缩略图
    private String thumb_c;
    //评论数
    private String comment_count;
    //作者
    private Author author;
    //自定义字段
    private CustomFields custom_fields;
    //标签
    private Tags tags;

    public FreshNews() {
    }

    public static String getUrlFreshNews(int page) {
        return URL_FRESH_NEWS + page;
    }

    public static String getUrlFreshNewsDetail(String id) {
        return URL_FRESH_NEWS_DETAIL + id;
    }


    public static ArrayList<FreshNews> parse(JSONArray postsArray) {

        ArrayList<FreshNews> freshNewses = new ArrayList<>();

        for (int i = 0; i < postsArray.length(); i++) {

            FreshNews freshNews = new FreshNews();
            JSONObject jsonObject = postsArray.optJSONObject(i);

            freshNews.setId(jsonObject.optString("id"));
            freshNews.setUrl(jsonObject.optString("url"));
            freshNews.setTitle(jsonObject.optString("title"));
            freshNews.setDate(jsonObject.optString("date"));
            freshNews.setComment_count(jsonObject.optString("comment_count"));
            freshNews.setAuthor(Author.parse(jsonObject.optJSONObject("author")));
            freshNews.setCustomFields(CustomFields.parse(jsonObject.optJSONObject("custom_fields")));
            freshNews.setTags(Tags.parse(jsonObject.optJSONArray("tags")));

            freshNewses.add(freshNews);

        }
        return freshNewses;
    }


    public static ArrayList<FreshNews> parseCache(JSONArray postsArray) {

        ArrayList<FreshNews> freshNewses = new ArrayList<>();

        for (int i = 0; i < postsArray.length(); i++) {

            FreshNews freshNews = new FreshNews();
            JSONObject jsonObject = postsArray.optJSONObject(i);

            freshNews.setId(jsonObject.optString("id"));
            freshNews.setUrl(jsonObject.optString("url"));
            freshNews.setTitle(jsonObject.optString("title"));
            freshNews.setDate(jsonObject.optString("date"));
            freshNews.setComment_count(jsonObject.optString("comment_count"));
            freshNews.setAuthor(Author.parse(jsonObject.optJSONObject("author")));
            freshNews.setCustomFields(CustomFields.parseCache(jsonObject.optJSONObject("custom_fields")));
            freshNews.setTags(Tags.parseCache(jsonObject.optJSONObject("tags")));

            freshNewses.add(freshNews);

        }
        return freshNewses;
    }

    @Override
    public String toString() {
        return "FreshNews{" +
                "tags=" + tags +
                ", customFields=" + custom_fields +
                ", author=" + author +
                ", comment_count='" + comment_count + '\'' +
                ", thumb_c='" + thumb_c + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumb_c() {
        return thumb_c;
    }

    public void setThumb_c(String thumb_c) {
        this.thumb_c = thumb_c;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public CustomFields getCustomFields() {
        return custom_fields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.custom_fields = customFields;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
}

package com.socks.jiandan.model;

/**
 * 新鲜事
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FreshNews {

	public static final String URL_FRESH_NEWS = "http://jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1&page=";

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
	private CustomFields customFields;
	//标签
	private Tags tags;

	public FreshNews() {
	}

	public FreshNews(String id, String title, String url, String date, String thumb_c, String
			comment_count, Author author, CustomFields customFields, Tags tags) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.date = date;
		this.thumb_c = thumb_c;
		this.comment_count = comment_count;
		this.author = author;
		this.customFields = customFields;
		this.tags = tags;
	}

	public static String getUrlFreshNews(int page) {
		return URL_FRESH_NEWS + page;
	}

	@Override
	public String toString() {
		return "FreshNews{" +
				"tags=" + tags +
				", customFields=" + customFields +
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
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}
}

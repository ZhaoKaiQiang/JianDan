package com.socks.jiandan.model;

import com.socks.jiandan.view.floorview.Commentable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * 新鲜事评论
 * Created by zhaokaiqiang on 15/4/27.
 */
public class Comment4FreshNews implements Comparable ,Commentable {

	//评论列表
	public static final String URL_COMMENTS = "http://jandan.net/?oxwlxojflwblxbsapi=get_post&include=comments&id=";
	//对新鲜事发表评论
	public static final String URL_PUSH_COMMENT ="http://jandan.net/?oxwlxojflwblxbsapi=respond.submit_comment";

	//评论布局类型
	public static final int TYPE_HOT = 0;
	public static final int TYPE_NEW = 1;
	public static final int TYPE_NORMAL = 2;

	//评论内容标签
	public static final String TAG_HOT = "hot";
	public static final String TAG_NORMAL = "normal";

	private int id;
	private String name;
	private String url;
	private String date;
	private String content;
	//没有用到
	private String parent;
	private int parentId;
	private ArrayList<Comment4FreshNews> parentComments;
	private int vote_positive;
	private int vote_negative;
	private int index;

	//用于区别布局类型：热门评论、最新评论、普通评论
	private int type=TYPE_NORMAL;
	//所属楼层
	private int floorNum=1;
	//用于标示是否是热门评论
	private String tag=TAG_NORMAL;

	public Comment4FreshNews() {
	}

	public Comment4FreshNews(int id, String name, String url, String date, String content, String parent, int vote_positive, int vote_negative, int index, int type, int floorNum, String tag) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.date = date;
		this.content = content;
		this.parent = parent;
		this.vote_positive = vote_positive;
		this.vote_negative = vote_negative;
		this.index = index;
		this.type = type;
		this.floorNum = floorNum;
		this.tag = tag;
	}

	/**
	 * 获取评论地址
	 *
	 * @param id
	 * @return
	 */
	public static String getUrlComments(String id) {
		return URL_COMMENTS + id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static String getURL_COMMENTS() {
		return URL_COMMENTS;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getVote_positive() {
		return vote_positive;
	}

	public void setVote_positive(int vote_positive) {
		this.vote_positive = vote_positive;
	}

	public int getVote_negative() {
		return vote_negative;
	}

	public void setVote_negative(int vote_negative) {
		this.vote_negative = vote_negative;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ArrayList<Comment4FreshNews> getParentComments() {
		return parentComments;
	}

	public void setParentComments(ArrayList<Comment4FreshNews> parentComments) {
		this.parentComments = parentComments;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}



	@Override
	public String toString() {
		return "Comment4FreshNews{" +
				"id=" + id +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", date='" + date + '\'' +
				", content='" + content + '\'' +
				", parent='" + parent + '\'' +
				", vote_positive=" + vote_positive +
				", vote_negative=" + vote_negative +
				", index=" + index +
				", type=" + type +
				", floorNum=" + floorNum +
				", tag='" + tag + '\'' +
				'}';
	}

	@Override
	public int compareTo(Object another) {
		String anotherTimeString = ((Comment4FreshNews) another).getDate();
		String thisTimeString = getDate();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

		try {
			Date anotherDate = simpleDateFormat.parse(anotherTimeString);
			Date thisDate = simpleDateFormat.parse(thisTimeString);
			return -thisDate.compareTo(anotherDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getCommentFloorNum() {
		return getFloorNum();
	}

	@Override
	public String getCommentContent() {
		return getContent();
	}

	@Override
	public String getAuthorName() {
		return getName();
	}
}

package com.socks.jiandan.model;

import com.socks.jiandan.view.floorview.Commentable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhaokaiqiang on 15/4/10.
 */
public class Commentator implements Comparable ,Commentable{

	//评论列表
	public static final String URL_COMMENT_LIST = "http://jandan.duoshuo.com/api/threads/listPosts.json?thread_key=";
	//发表评论
	public static final String URL_PUSH_COMMENT = "http://jandan.duoshuo.com/api/posts/create.json";

	//评论内容标签
	public static final String TAG_HOT = "hot";
	public static final String TAG_NORMAL = "normal";

	//评论布局类型
	public static final int TYPE_HOT = 0;
	public static final int TYPE_NEW = 1;
	public static final int TYPE_NORMAL = 2;

	private String avatar_url;
	private String created_at;
	private String name;
	private String message;

	//评论发送者id
	private String post_id;
	//这条评论所回复的评论id
	private String parent_id;
	//这条评论上的所有评论id
	private String[] parents;
	//所属楼层
	private int floorNum;

	//用于标示是否是热门评论
	private String tag;
	//用于区别布局类型：热门评论、最新评论、普通评论
	private int type;

	public Commentator() {
	}

	public Commentator(String avatar_url, String created_at, String name, String message, String
			post_id, String parent_id, String [] parents, int floorNum, String tag, int type) {
		this.avatar_url = avatar_url;
		this.created_at = created_at;
		this.name = name;
		this.message = message;
		this.post_id = post_id;
		this.parent_id = parent_id;
		this.parents = parents;
		this.floorNum = floorNum;
		this.tag = tag;
		this.type = type;
	}

	/**
	 * 获取评论列表详细数据的URL
	 *
	 * @param thread_id
	 * @return
	 */
	public static String getUrlCommentList(String thread_id) {
		return URL_COMMENT_LIST + thread_id;
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

	@Override
	public int compareTo(Object another) {

		String anotherTimeString = ((Commentator) another).getCreated_at().replace("T", " ");
		anotherTimeString = anotherTimeString.substring(0, anotherTimeString.indexOf("+"));

		String thisTimeString = getCreated_at().replace("T", " ");
		thisTimeString = thisTimeString.substring(0, thisTimeString.indexOf("+"));

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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public String[] getParents() {
		return parents;
	}

	public void setParents(String[] parents) {
		this.parents = parents;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	@Override
	public int getCommentFloorNum() {
		return getFloorNum();
	}

	@Override
	public String getCommentContent() {
		return getMessage();
	}

	@Override
	public String getAuthorName() {
		return getName();
	}
}

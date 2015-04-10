package com.socks.jiandan.model;

import android.support.v4.app.Fragment;

/**
 * Created by zhaokaiqiang on 15/4/9.
 */
public class MenuItem {

	private String title;
	private int resourceId;
	private Class<? extends Fragment> fragment;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public Class<? extends Fragment> getFragment() {
		return fragment;
	}

	public void setFragment(Class<? extends Fragment> fragment) {
		this.fragment = fragment;
	}

	public MenuItem(){};

	public MenuItem(String title, int resourceId, Class<? extends Fragment> fragment) {
		this.resourceId = resourceId;
		this.title = title;
		this.fragment = fragment;
	}

}
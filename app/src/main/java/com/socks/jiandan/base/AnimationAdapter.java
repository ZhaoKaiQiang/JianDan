package com.socks.jiandan.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.socks.jiandan.R;

/**
 * 我其实是想抽取出来的所有带动画的Adapter的，但是没成功，类型不对
 * Created by zhaokaiqiang on 15/5/4.
 */
public class AnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	protected int lastPosition = -1;

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}

	protected void setAnimation(View viewToAnimate, int position) {
		if (position > lastPosition) {
			Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
					.anim.item_bottom_in);
			viewToAnimate.startAnimation(animation);
			lastPosition = position;
		}
	}
}

package com.socks.jiandan.view;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/4/24.
 */
public class FadeInAnimator extends RecyclerView.ItemAnimator{

	private ArrayList<RecyclerView.ViewHolder> viewHolders;

	public FadeInAnimator(){
		viewHolders = new ArrayList<>();
	}


	@Override
	public void runPendingAnimations() {

	}

	@Override
	public boolean animateRemove(RecyclerView.ViewHolder holder) {
		return false;
	}

	@Override
	public boolean animateAdd(RecyclerView.ViewHolder holder) {

		return false;
	}

	@Override
	public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
		return false;
	}

	@Override
	public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
		return false;
	}

	@Override
	public void endAnimation(RecyclerView.ViewHolder item) {

	}

	@Override
	public void endAnimations() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}
}

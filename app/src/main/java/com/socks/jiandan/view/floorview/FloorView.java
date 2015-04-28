/**
 *
 */
package com.socks.jiandan.view.floorview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.jiandan.R;

import java.util.Iterator;

/**
 * @author JohnnyShieh
 * @ClassName: FloorView
 * @Description:
 * @date Jan 25, 2014 2:09:36 PM
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FloorView extends LinearLayout {

	private int density;
	private Drawable drawer;
	private SubComments datas;
	private SubFloorFactory factory;

	public FloorView(Context context) {
		super(context);
		init(context);
	}

	public FloorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FloorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void setBoundDrawer(Drawable drawable) {
		drawer = drawable;
	}

	public void setComments(SubComments cmts) {
		datas = cmts;
	}

	public void setFactory(SubFloorFactory fac) {
		factory = fac;
	}

	public int getFloorNum() {
		return getChildCount();
	}

	private void init(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		density = (int) (3.0F * context.getResources().getDisplayMetrics().density);
	}

	public void init() {
		if (null == datas.iterator())
			return;
		if (datas.getFloorNum() < 7) {
			for (Iterator<? extends Commentable> iterator = datas.iterator(); iterator
					.hasNext(); ) {
				View view = factory.buildSubFloor(iterator.next(), this);
				addView(view);
			}
		} else {
			View view;
			view = factory.buildSubFloor(datas.get(0), this);
			addView(view);
			view = factory.buildSubFloor(datas.get(1), this);
			addView(view);
			view = factory.buildSubHideFloor(datas.get(2), this);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TextView hide_text = (TextView) v
							.findViewById(R.id.hide_text);
					hide_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
					v.findViewById(R.id.hide_pb).setVisibility(View.VISIBLE);
					removeAllViews();
					for (Iterator<? extends Commentable> iterator = datas.iterator(); iterator
							.hasNext(); ) {
						View view = factory.buildSubFloor(iterator.next(),
								FloorView.this);
						addView(view);
					}
					reLayoutChildren();
				}
			});
			addView(view);
			view = factory.buildSubFloor(datas.get(datas.size() - 1), this);
			addView(view);
		}

		reLayoutChildren();
	}

	public void reLayoutChildren() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			layout.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			int margin = Math.min((count - i - 1), 4) * density;
			layout.leftMargin = margin;
			layout.rightMargin = margin;
			if (i == count - 1) {
				layout.topMargin = 0;
			} else {
				layout.topMargin = Math.min((count - i), 4) * density;
			}
			view.setLayoutParams(layout);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int i = getChildCount();
		if (null != drawer && i > 0) {
			for (int j = i - 1; j >= 0; j--) {
				View view = getChildAt(j);
				drawer.setBounds(view.getLeft(), view.getLeft(),
						view.getRight(), view.getBottom());
				drawer.draw(canvas);
			}
		}
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.getChildCount() <= 0) {
			setMeasuredDimension(0, 0);
			return;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}

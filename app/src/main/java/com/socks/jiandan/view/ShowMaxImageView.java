package com.socks.jiandan.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.socks.jiandan.utils.ScreenSizeUtil;

/**
 * 自定义控件，用于显示宽度和ImageView相同，高度自适应的图片显示模式.
 * 除此之外，还添加了最大高度限制，若图片长度大于等于屏幕长度，则高度显示为屏幕的1/3
 * Created by zhaokaiqiang on 15/4/20.
 */
public class ShowMaxImageView extends ImageView {

	private Bitmap mBitmap;

	public ShowMaxImageView(Context context) {
		super(context);
	}

	public ShowMaxImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowMaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			mBitmap = getMatrixBitmap(bm);
		}
		super.setImageBitmap(bm);
		requestLayout();
	}


	@Override
	public void setImageDrawable(Drawable drawable) {
		if (drawable != null) {
			mBitmap = getMatrixBitmap(drawableToBitamp(drawable));
		}
		super.setImageDrawable(drawable);
		requestLayout();
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (mBitmap != null) {
			int resultWidth = 0;
			int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
			int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

			if (modeWidth == MeasureSpec.EXACTLY) {
				resultWidth = sizeWidth;
			} else {
				resultWidth = mBitmap.getWidth();
				if (modeWidth == MeasureSpec.AT_MOST) {
					resultWidth = Math.max(resultWidth, sizeWidth);
				}
			}

			int resultHeight = 0;
			int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
			int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

			if (modeHeight == MeasureSpec.EXACTLY) {
				resultHeight = sizeHeight;
			} else {
				resultHeight = mBitmap.getHeight();
				if (modeHeight == MeasureSpec.AT_MOST) {
					resultHeight = Math.max(resultHeight, sizeHeight);
				}

				if (resultHeight >= ScreenSizeUtil.getScreenHeight((Activity) getContext())) {
					resultHeight = ScreenSizeUtil.getScreenHeight((Activity) getContext()) / 3;
				}

			}
			setMeasuredDimension(resultWidth, resultHeight);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	private Bitmap getMatrixBitmap(Bitmap bm) {
		float width = getWidth();
		int bitmapWidth = bm.getWidth();
		int bitmapHeight = bm.getHeight();

		if (bitmapWidth > 0 && bitmapHeight > 0) {
			float scaleWidth = width / bitmapWidth;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			return Bitmap.createBitmap(bm, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
		} else {
			return null;
		}
	}

	private Bitmap drawableToBitamp(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();
	}

}

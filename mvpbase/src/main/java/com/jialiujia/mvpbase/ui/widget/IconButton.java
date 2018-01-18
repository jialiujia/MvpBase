package com.jialiujia.mvpbase.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.jialiujia.mvpbase.R;

/**
 * MvpBase
 * 图标按钮，解决图标无法与文字中间水平排放问题
 * Created by Administrator on 2018/1/17.
 */

public class IconButton extends Button {
	protected int drawableWidth;  //图片宽度
	protected DrawablePositions drawablePositions;  //图片位置
	protected int iconPadding;  //文字与图片间距

	//缓存用,防止重新分配内存
	Rect bounds;

	public IconButton(Context context) {
		super(context);
		bounds = new Rect();
	}

	public IconButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		bounds = new Rect();
		applyAttributes(attrs);
	}

	public IconButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		bounds = new Rect();
		applyAttributes(attrs);
	}

	protected void applyAttributes(AttributeSet attrs) {
		if (bounds == null) {
			bounds = new Rect();
		}

		TypedArray typedArray = getContext()
				.obtainStyledAttributes(attrs, R.styleable.IconButton);
		int paddingId = typedArray
				.getDimensionPixelSize(R.styleable.IconButton_iconPadding, 0);
		setIconPadding(paddingId);
		typedArray.recycle();
	}

	public void setIconPadding(int padding) {
		iconPadding = padding;
		requestLayout();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		Paint textPaint = getPaint();
		String text = getText().toString();
		textPaint.getTextBounds(text, 0, text.length(), bounds);

		int textWidth = bounds.width();
		int factor = (drawablePositions == DrawablePositions.LEFT_AND_RIGHT) ? 2 : 1;
		int contentWidth = textWidth + drawableWidth + iconPadding * factor;
		int horizontalPadding = (int)((getWidth() / 2.0) - (contentWidth / 2.0));
		setCompoundDrawablePadding(-horizontalPadding + iconPadding);

		switch (drawablePositions) {
			case LEFT:
				setPadding(horizontalPadding, getPaddingTop(), 0, getPaddingBottom());
				break;
			case RIGHT:
				setPadding(0, getPaddingTop(), horizontalPadding, getPaddingBottom());
				break;
			case LEFT_AND_RIGHT:
				setPadding(horizontalPadding, getPaddingTop(), horizontalPadding, getPaddingBottom());
				break;
			default:
				setPadding(0, getPaddingTop(), 0, getPaddingBottom());
				break;
		}
	}

	@Override
	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top,
	                                                    Drawable right, Drawable bottom) {
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

		if (left != null && right != null) {
			drawableWidth = left.getIntrinsicWidth() + right.getIntrinsicWidth();
			drawablePositions = DrawablePositions.LEFT_AND_RIGHT;
		} else if (left != null) {
			drawableWidth = left.getIntrinsicWidth();
			drawablePositions = DrawablePositions.LEFT;
		} else if (right != null) {
			drawableWidth = right.getIntrinsicWidth();
			drawablePositions = DrawablePositions.RIGHT;
		} else {
			drawablePositions = DrawablePositions.NONE;
		}

		requestLayout();
	}

	private static enum DrawablePositions {
		NONE,
		LEFT_AND_RIGHT,
		LEFT,
		RIGHT
	}
}

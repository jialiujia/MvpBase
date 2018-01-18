package com.jialiujia.mvpbase.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * MvpBase
 * iconfont字体显示
 * Created by Administrator on 2018/1/17.
 */

public class TypefaceTextView extends TextView {

	Typeface iconfont;

	/**
	 * 默认路径asset/iconfont/iconfont.ttf
	 * @param context/
	 */
	public TypefaceTextView(Context context) {
		super(context);
		iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
		if(iconfont != null) {
			setTypeface(iconfont);
		}
	}

	/**
	 * 默认路径asset/iconfont/iconfont.ttf
	 * @param context/
	 * @param attrs/
	 */
	public TypefaceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
		if(iconfont != null) {
			setTypeface(iconfont);
		}
	}

	/**
	 * 默认路径asset/iconfont/iconfont.ttf
	 * @param context/
	 * @param attrs/
	 * @param defStyleAttr/
	 */
	public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
		if(iconfont != null) {
			setTypeface(iconfont);
		}
	}

	/**
	 * 默认路径asset/iconfont/iconfont.ttf
	 * @param context /
	 * @param attrs/
	 * @param defStyleAttr/
	 * @param defStyleRes/
	 */
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
		if(iconfont != null) {
			setTypeface(iconfont);
		}
	}
}

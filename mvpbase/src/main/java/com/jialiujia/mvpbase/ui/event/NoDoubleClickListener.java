package com.jialiujia.mvpbase.ui.event;

import android.view.View;

import java.util.Calendar;

/**
 * MvpBase
 * 点击监听事件，防止点击过快造成执行多次
 * Created by Administrator on 2018/1/17.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
	private static int MIN_CLICK_DELAY_TIME = 0;
	private long lastClickTime = 0;

	/**
	 * 初始化,默认200毫秒
	 */
	protected NoDoubleClickListener() {
		MIN_CLICK_DELAY_TIME = 200;
	}

	/**
	 * 初始化
	 * @param clickDelatTime    自定义间隔时间
	 */
	protected NoDoubleClickListener(int clickDelatTime) {
		MIN_CLICK_DELAY_TIME = clickDelatTime;
	}

	@Override
	public void onClick(View v) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			lastClickTime = currentTime;
			onNoDoubleClick(v);
		}
	}

	protected abstract void onNoDoubleClick(View v);
}

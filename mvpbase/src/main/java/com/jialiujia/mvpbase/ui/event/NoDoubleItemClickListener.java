package com.jialiujia.mvpbase.ui.event;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * MvpBase
 * 子项监听事件，防止点击过快造成执行多次
 * Created by Administrator on 2018/1/17.
 */

public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {

	private static int MIN_CLICK_DELAY_TIME = 0;
	private long lastClickTime = 0;

	/**
	 * 初始化,默认200毫秒
	 */
	protected NoDoubleItemClickListener() {
		MIN_CLICK_DELAY_TIME = 200;
	}

	/**
	 * 初始化
	 * @param clickDelatTime    自定义间隔时间
	 */
	protected NoDoubleItemClickListener(int clickDelatTime) {
		MIN_CLICK_DELAY_TIME = clickDelatTime;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			lastClickTime = currentTime;
			onNoDoubleItemClick(parent, view, position, id);
		}
	}

	protected abstract void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id);
}

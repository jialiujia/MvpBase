package com.jialiujia.mvpbase.ui.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * AndroidPos
 * Created by Administrator on 2018/5/14.
 */

@SuppressLint("AppCompatCustomView")
public class LongClickButton extends Button {
	/**
	 * 记录当前自定义Btn是否按下
	 */
	private boolean clickdown = false;

	/**
	 * 长按的回调接口
	 */
	private LongClickListener mListener;

	/**
	 * 按钮长按时 间隔多少毫秒来处理 回调方法
	 */
	private int mtime;

	public LongClickButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 处理touch事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			clickdown = true;
			new LongTouchTask().execute();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP) {
			clickdown = false;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 处理长按的任务
	 */
	class  LongTouchTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			while(clickdown) {
				sleep(mtime);
				publishProgress(0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (clickdown) {
				mListener.onLongClick();
			}
		}

	}

	/**
	 * 给长按btn控件注册一个监听器。
	 *
	 * @param listener
	 *            监听器的实现。
	 * @param time
	 *            多少毫秒时间间隔 来处理一次回调方法
	 */
	public void setOnLongTouchListener(LongClickListener listener, int time) {
		mListener = listener;
		mtime = time;

	}

	/**
	 * 使当前线程睡眠指定的毫秒数。
	 *
	 * @param time
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 长按监听接口，使用按钮长按的地方应该注册此监听器来获取回调。
	 */
	public interface LongClickListener {

		/**
		 * 处理长按的回调方法
		 */
		void onLongClick();
	}
}

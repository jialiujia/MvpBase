package com.jialiujia.mvpbase.ui.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * MvpBase
 * Created by Administrator on 2018/1/18.
 */

public class ViewHolder {

	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(View parent, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) parent.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<>();
			parent.setTag(viewHolder);
		}

		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = parent.findViewById(id);
			viewHolder.put(id, childView);
		}

		return (T) childView;
	}

	@SuppressWarnings("unchecked")
	public static void removeViews(View parent) {
		SparseArray<View> viewHolder = (SparseArray<View>) parent.getTag();
		if (viewHolder != null) {
			viewHolder.clear();
		}
	}
}

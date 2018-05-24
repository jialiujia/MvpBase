package com.jialiujia.mvpbase.core.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class ActivityUtils {

	/**
	 * 绑定Fragment到Activity
	 */
	public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
	                                         @NonNull Fragment fragment, int frameId) throws Exception {
		if (fragmentManager == null || fragment == null) {
			throw new Exception("arg is null");
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(frameId, fragment);
		transaction.commit();
	}

	/**
	 * 绑定Fragment到Activity
	 * @param fragmentManager
	 * @param fragment
	 * @param frameId
	 * @param tag
	 * @param addTobackStack
	 * @throws Exception
	 */
	public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
	                                         @NonNull Fragment fragment, int frameId,
	                                         String tag, boolean addTobackStack, String backTag) throws Exception {
		if (fragmentManager == null || fragment == null) {
			throw new Exception("arg is null");
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(frameId, fragment, tag);
		if (addTobackStack) {
			transaction.addToBackStack(backTag);
		}
		transaction.commit();
	}

	/**
	 * 在Activity替换Fragment
	 * @param fragmentManager
	 * @param to
	 * @param frameId
	 * @param tag
	 * @param addTobackStack
	 * @throws Exception
	 */
	public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
	                                             @NonNull Fragment to, int frameId,
	                                             String tag, boolean addTobackStack, String backTag) throws Exception {
		if (fragmentManager == null || to == null) {
			throw new Exception("arg is null");
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(frameId, to, tag);
		if (addTobackStack) {
			transaction.addToBackStack(backTag);
		}
		transaction.commit();
	}

	/**
	 * 在Activity替换Fragment
	 * @param fragmentManager
	 * @param from
	 * @param to
	 * @param frameId
	 * @throws Exception
	 */
	public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
	                                             @NonNull Fragment from, Fragment to, int frameId,
	                                             String tag, boolean needResume) throws Exception {
		if (fragmentManager == null || from == null || to == null) {
			throw new Exception("arg is null");
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!to.isAdded()) {
			transaction.hide(from).add(frameId, to, tag);
			transaction.commit();
		} else {
			transaction.hide(from).show(to).commit();
			if (needResume) {
				to.onResume();
			}
		}
	}

	/**
	 * 在Activity回退Fragment
	 * @param fragmentManager
	 * @param tag
	 * @throws Exception
	 */
	public static void popStack(@NonNull FragmentManager fragmentManager, String tag) throws Exception {
		if (fragmentManager == null) {
			throw new Exception("arg is null");
		}
		if (tag != null) {
			fragmentManager.popBackStack(tag, -1);
		} else {
			fragmentManager.popBackStack();
		}
	}

	/**
	 * 校验某个服务是否还存在
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceAlive(Context context, String serviceName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(100);
		for (ActivityManager.RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(serviceName)) {
				return true;
			}
		}

		return false;
	}
}

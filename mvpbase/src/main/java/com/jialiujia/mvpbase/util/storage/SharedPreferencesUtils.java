package com.jialiujia.mvpbase.util.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * MvpBase
 * Created by Administrator on 2018/4/15.
 */

public class SharedPreferencesUtils {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "shared_data";

	/**
	 * 数据存储
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void put(Context context, String key, Object value) {
		put(context, FILE_NAME, key, value);
	}

	/**
	 * 数据存储
	 * @param context
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void put(Context context, String fileName, String key, Object value) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		if (value instanceof String) {
			editor.putString(key, (String)value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (boolean)value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (int)value);
		} else if (value instanceof Long) {
			editor.putLong(key, (long)value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (float)value);
		} else if (value instanceof Double) {
			editor.putFloat(key, (float)value);
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 数据读取
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultValue) {
		return get(context, FILE_NAME, key, defaultValue);
	}

	/**
	 * 数据读取
	 * @param context
	 * @param fileName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(Context context, String fileName, String key, Object defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

		if (defaultValue instanceof String) {
			return sharedPreferences.getString(key, (String)defaultValue);
		} else if (defaultValue instanceof Boolean) {
			return sharedPreferences.getBoolean(key, (boolean)defaultValue);
		} else if (defaultValue instanceof Integer) {
			return sharedPreferences.getInt(key, (int)defaultValue);
		} else if (defaultValue instanceof Long) {
			return sharedPreferences.getLong(key, (long)defaultValue);
		} else if (defaultValue instanceof Float) {
			return sharedPreferences.getFloat(key, (float)defaultValue);
		} else if (defaultValue instanceof Double) {
			return sharedPreferences.getFloat(key, (float)defaultValue);
		}

		return null;
	}

	/**
	 * 移除key
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		remove(context, FILE_NAME, key);
	}

	/**
	 * 移除key
	 * @param context
	 * @param fileName
	 * @param key
	 */
	public static void remove(Context context, String fileName, String key) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * @param context
	 */
	public static void clear(Context context) {
		clear(context, FILE_NAME);
	}

	/**
	 * 清除所有数据
	 * @param context
	 * @param fileName
	 */
	public static void clear(Context context, String fileName) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询是否存在key
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		return contains(context, FILE_NAME, key);
	}

	/**
	 * 查询是否存在key
	 * @param context
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String fileName, String key) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return sharedPreferences.contains(key);
	}

	/**
	 * 读取所有数据
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		return getAll(context, FILE_NAME);
	}

	/**
	 * 读取所有数据
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Map<String, ?> getAll(Context context, String fileName) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return sharedPreferences.getAll();
	}

	/**
	 * apply方法兼容处理
	 */
	private static class SharedPreferencesCompat {

		private static final Method applyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * @return
		 */
		@SuppressWarnings({"unchecked", "rawtypes"})
		private static Method findApplyMethod() {
			try {
				Class clazz = SharedPreferences.Editor.class;
				return clazz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 兼容运行apply方法
		 * @param editor
		 * @return
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (applyMethod != null) {
					applyMethod.invoke(editor);
				}
			} catch (IllegalArgumentException
					| IllegalAccessException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			//若editor没有apply，则用commit
			editor.commit();
		}
	}
}

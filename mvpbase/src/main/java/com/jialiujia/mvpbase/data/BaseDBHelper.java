package com.jialiujia.mvpbase.data;

import android.content.ContentValues;

/**
 * MvpBase
 * Created by Administrator on 2018/1/15.
 */

public interface BaseDBHelper {
	boolean insertSingleData(String tableName, ContentValues value);

	boolean insertMultiData(String tableName, ContentValues[] values);

	boolean updata(String tableName, ContentValues value, String whereClause,
	               String[] whereArgs);

	boolean deleteForm(String tableName);

	boolean delete(String tableName, String whereClause, String[] whereArgs);

	String query(boolean distinct, String table, String[] columns, String selection,
	             String[] whereArgs, String groupBy,
	             String having, String orderBy, String limit);

	<T> T query(boolean distinct, String table, String[] columns, String selection,
	            String[] whereArgs, String groupBy,
	            String having, String orderBy, String limit,Class<T> clazz);

	void executeSQL(String sql);
}

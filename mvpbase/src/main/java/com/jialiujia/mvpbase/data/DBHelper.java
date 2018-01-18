package com.jialiujia.mvpbase.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作封装
 * Created by Jialiujia on 2018/1/15.
 */

public abstract class DBHelper extends SQLiteOpenHelper implements BaseDBHelper {
	public DBHelper(Context context, String name){
		super(context, name, null, 1);
	}

	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		createDB(sqLiteDatabase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		updataDB(sqLiteDatabase,i,i1);
	}

	protected abstract void createDB(SQLiteDatabase db);

	protected abstract void updataDB(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer);

	/**
	 * 删除表数据
	 * @param tableName     //表名
	 * @param whereClause   //字段名
	 * @param whereArgs     //字段值
	 * @return               //成功标志
	 */
	@Override
	public boolean delete(String tableName, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		boolean result = false;
		db.beginTransaction();
		try {
			int t = db.delete(tableName, whereClause, whereArgs);
			if (t <= 0) {
				result = false;
			} else {
				db.setTransactionSuccessful();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 插入单条数据
	 * @param tableName     //表名
	 * @param value         //键值对
	 * @return              //成功标志
	 */
	@Override
	public boolean insertSingleData(String tableName, ContentValues value) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		boolean result = false;
		try {
			long tresult = db.insert(tableName, null, value);
			if (tresult == -1) {
				result = false;
			} else {
				db.setTransactionSuccessful();
				result = true;
			}
			value.clear();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 插入多条数据
	 * @param tableName     //表名
	 * @param values        //键值对数组
	 * @return              //成功标志
	 */
	@Override
	public boolean insertMultiData(String tableName, ContentValues[] values) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO " + tableName + " VALUES( ";
		for (int i = 0; i < values[0].keySet().size(); i++) {
			if (i == values[0].keySet().size() - 1) {
				sql += "? )";
			} else {
				sql += "?,";
			}
		}
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
		boolean result = false;
		try {
			for (ContentValues value : values) {
				String[] keys = (String[]) value.keySet().toArray();
				for (int i = 0; i < value.keySet().size(); i++) {
					statement.bindString(i, value.getAsString(keys[i]));
				}
				statement.executeInsert();
			}
			db.setTransactionSuccessful();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 更新表数据
	 * @param tableName     //表名
	 * @param value         //键值对
	 * @param whereClause  //字段名
	 * @param whereArgs    //字段值
	 * @return              //成功标志
	 */
	@Override
	public boolean updata(String tableName, ContentValues value, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		boolean result = false;
		db.beginTransaction();
		try {
			int t = db.update(tableName, value, whereClause, whereArgs);
			if (t <= 0) {
				result = false;
			} else {
				db.setTransactionSuccessful();
				result = true;
			}
			value.clear();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 删除表
	 * @param tableName     //表名
	 * @return              //成功标志
	 */
	@Override
	public boolean deleteForm(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		boolean result = false;
		db.beginTransaction();
		try {
			String sql = "DELETE FROM " + tableName;
			db.execSQL(sql);
			db.setTransactionSuccessful();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			db.endTransaction();
			db.close();
		}

		return result;
	}

	/**
	 * 查询表数据
	 * @param distinct      //合并结果
	 * @param table         //表名
	 * @param columns       //列名
	 * @param selection     //选择列
	 * @param whereArgs     //字段名
	 * @param groupBy       //
	 * @param having        //
	 * @param orderBy       //
	 * @param limit         //限制数量
	 * @return              //json字符串
	 */
	@Override
	public String query(boolean distinct, String table, String[] columns,
	                    String selection, String[] whereArgs, String groupBy,
	                    String having, String orderBy, String limit) {
		String result;
		SQLiteDatabase db = getReadableDatabase();
		db.beginTransaction();
		try {
			Cursor cursor = db.query(distinct, table, columns, selection,
					whereArgs, groupBy, having, orderBy, limit);
			if (cursor != null && cursor.getCount() > 0) {
				String[] names = cursor.getColumnNames();
				List<Map<String,String>> list = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				do {
					Map<String,String> map = new HashMap<>();
					for (String name:names){
						map.put(name,cursor.getString(cursor.getColumnIndex(name)));
					}
					list.add(map);
				} while (cursor.moveToNext());
				result = new Gson().toJson(list);
				cursor.close();
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 查询表数据（返回实例）
	 * @param distinct      //合并结果
	 * @param table         //表名
	 * @param columns       //列名
	 * @param selection     //选择列
	 * @param whereArgs     //字段名
	 * @param groupBy       //
	 * @param having        //
	 * @param orderBy       //
	 * @param limit         //限制数量
	 * @param clazz         //转换类
	 * @param <T>           //转换泛型
	 * @return              //转换实例
	 */
	@Override
	public <T> T query(boolean distinct, String table, String[] columns,
	                   String selection, String[] whereArgs, String groupBy,
	                   String having, String orderBy, String limit, Class<T> clazz) {
		T result;
		SQLiteDatabase db = getReadableDatabase();
		db.beginTransaction();

		db.beginTransaction();
		try {
			Cursor cursor = db.query(distinct, table, columns, selection,
					whereArgs, groupBy, having, orderBy, limit);
			if (cursor != null && cursor.getCount() > 0) {
				String[] names = cursor.getColumnNames();
				List<Map<String,String>> list = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				do {
					Map<String,String> map = new HashMap<>();
					for (String name:names){
						map.put(name,cursor.getString(cursor.getColumnIndex(name)));
					}
					list.add(map);
				} while (cursor.moveToNext());
				String json = new Gson().toJson(list);
				result = new Gson().fromJson(json,clazz);
				cursor.close();
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	/**
	 * 执行SQL语句
	 * @param sql       //sql字符串
	 */
	@Override
	public void executeSQL(String sql) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
}

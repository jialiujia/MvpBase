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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据库操作封装
 * Created by Jialiujia on 2018/1/15.
 */

public abstract class DBHelper extends SQLiteOpenHelper implements BaseDBHelper {
	private AtomicInteger mOpenCounter; //原子锁，多线程操作用
	private SQLiteDatabase mDatabase;

	public DBHelper(Context context, String name){
		super(context, name, null, 1);
		mOpenCounter = new AtomicInteger();
	}

	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
		mOpenCounter = new AtomicInteger();
	}

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		mOpenCounter = new AtomicInteger();
	}

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		mOpenCounter = new AtomicInteger();
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
	 * 写数据库原子锁
	 */
	private synchronized SQLiteDatabase getAtomWritableDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			mDatabase = getWritableDatabase();
		}
		return mDatabase;
	}

	/**
	 * 读数据库原子锁
	 */
	private synchronized SQLiteDatabase getAtomReadableDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			mDatabase = getReadableDatabase();
		}
		return mDatabase;
	}

	/**
	 * 关闭数据库原子锁
	 */
	private synchronized void closeAtomDatabase() {
		if (mOpenCounter.incrementAndGet() == 0) {
			mDatabase.close();
		}
	}

	@Override
	public synchronized boolean delete(String tableName, String whereClause, String[] whereArgs) {
		mDatabase = getAtomWritableDatabase();
		boolean result = false;
		mDatabase.beginTransaction();
		try {
			int t = mDatabase.delete(tableName, whereClause, whereArgs);
			if (t <= 0) {
				result = false;
			} else {
				mDatabase.setTransactionSuccessful();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized boolean insertSingleData(String tableName, ContentValues value) {
		mDatabase = getAtomWritableDatabase();
		mDatabase.beginTransaction();
		boolean result = false;
		try {
			long tresult = mDatabase.insert(tableName, null, value);
			if (tresult == -1) {
				result = false;
			} else {
				mDatabase.setTransactionSuccessful();
				result = true;
			}
			value.clear();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized boolean insertMultiData(String tableName, ContentValues[] values) {
		mDatabase = getAtomWritableDatabase();
		String sql = "INSERT INTO " + tableName + " VALUES( ";
		for (int i = 0; i < values[0].keySet().size(); i++) {
			if (i == values[0].keySet().size() - 1) {
				sql += "? )";
			} else {
				sql += "?,";
			}
		}
		SQLiteStatement statement = mDatabase.compileStatement(sql);
		mDatabase.beginTransaction();
		boolean result = false;
		try {
			for (ContentValues value : values) {
				String[] keys = (String[]) value.keySet().toArray();
				for (int i = 0; i < value.keySet().size(); i++) {
					statement.bindString(i, value.getAsString(keys[i]));
				}
				statement.executeInsert();
			}
			mDatabase.setTransactionSuccessful();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized boolean updata(String tableName, ContentValues value, String whereClause, String[] whereArgs) {
		mDatabase = getAtomWritableDatabase();
		boolean result = false;
		mDatabase.beginTransaction();
		try {
			int t = mDatabase.update(tableName, value, whereClause, whereArgs);
			if (t <= 0) {
				result = false;
			} else {
				mDatabase.setTransactionSuccessful();
				result = true;
			}
			value.clear();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized boolean deleteForm(String tableName) {
		mDatabase = getAtomWritableDatabase();
		boolean result = false;
		mDatabase.beginTransaction();
		try {
			String sql = "DELETE FROM " + tableName;
			mDatabase.execSQL(sql);
			mDatabase.setTransactionSuccessful();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}

		return result;
	}

	@Override
	public synchronized String query(boolean distinct, String table, String[] columns,
	                                 String selection, String[] whereArgs, String groupBy,
	                                 String having, String orderBy, String limit) {
		String result;
		mDatabase = getReadableDatabase();
		mDatabase.beginTransaction();
		try {
			Cursor cursor = mDatabase.query(distinct, table, columns, selection,
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
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized <T> T query(boolean distinct, String table, String[] columns,
	                                String selection, String[] whereArgs, String groupBy,
	                                String having, String orderBy, String limit, Class<T> clazz) {
		T result;
		mDatabase = getReadableDatabase();
		mDatabase.beginTransaction();

		mDatabase.beginTransaction();
		try {
			Cursor cursor = mDatabase.query(distinct, table, columns, selection,
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
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
		return result;
	}

	@Override
	public synchronized void executeSQL(String sql) {
		mDatabase = getAtomWritableDatabase();
		mDatabase.beginTransaction();
		try {
			mDatabase.execSQL(sql);
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
			closeAtomDatabase();
		}
	}
}

package com.jialiujia.mvpbase.util.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class FileUtils {
	final static String TAG = "FileUtils";

	/**
	 * 判断文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path){
		File file = new File(path);
		if (!file.isDirectory()){
			return file.exists();
		}
		return false;
	}

	/**
	 * 获取文件大小
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			Log.e(TAG, "文件不存在!");
		}
		return size;
	}

	/**
	 * 获取文件夹大小
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 文件转Byte数组
	 * @param file
	 * @return
	 */
	public static byte[] File2Byte(File file){
		byte[] buff=null;
		try {
			FileInputStream fis=new FileInputStream(file);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte[] bytes = new byte[(int) file.length()];
			int n;
			if ((n=fis.read(bytes))!=-1){
				bos.write(bytes);
			}
			buff=bos.toByteArray();
			bos.flush();
			fis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buff;
	}

	public static void Byte2File(byte[] buf, String filePath, String fileName)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory())
			{
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
	 * @param context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files/
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，不要误删。而且只支持目录下的文件删除
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 删除指定目录下文件及目录
	 *
	 * @param deleteThisPath
	 * @param filePath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {// 如果下面还有文件
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 清除本应用所有的数据
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		if (filepath == null) {
			return;
		}
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/**
	 * 获取文件夹下的文件名
	 * @param folderName
	 * @return
	 */
	public static String[] getFileNameFromFolder(String folderName){
		File folder = new File(folderName);
		if (folder.exists() && folder.isDirectory()){
			return folder.list();
		}
		return null;
	}

	/**
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath){
		try {
			File file = new File(filePath);
			if (file.exists()){
				return file.delete();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 复制文件
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 */
	public static boolean copyFile(String oldFilePath, String newFilePath) {
		try {
			File oldFile = new File(oldFilePath);
			if (oldFile.exists()) {
				InputStream is = new FileInputStream(oldFilePath);
				FileOutputStream fos = new FileOutputStream(newFilePath);
				byte[] buff = new byte[1024 * 1024];
				int length;
				while ((length = is.read(buff)) != -1) {
					fos.write(buff);
				}
				is.close();
				fos.flush();
				fos.close();

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * File转为InputStream
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream file2InputStream(File file)
			throws FileNotFoundException {
		return new FileInputStream(file);
	}

	/**
	 * stream转为File
	 * @param stream
	 * @param file
	 * @param size
	 * @throws IOException
	 */
	public static void stram2File(InputStream stream,File file,int size)
			throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[size];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		stream.close();
	}

	/**
	 * stream转byte数组
	 * @param stream
	 * @return
	 */
	public static byte[] stream2Byte(InputStream stream,int size)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buff = new byte[size];
		int bytesRead = 0;
		while ((bytesRead = stream.read(buff, 0, 100)) > 0) {
			bos.write(buff, 0, bytesRead);
		}
		byte[]result = bos.toByteArray();
		bos.flush();
		bos.close();
		return result;
	}

	/**
	 * 格式化存储单位
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "B";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
}

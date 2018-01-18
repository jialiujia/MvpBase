package com.jialiujia.mvpbase.util.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class CompressUtils {
	private static final int BUFFER = 1024*1000*10;

	/**
	 * Gzip压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compressByGzip(byte[] data) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		compressByGzip(bos,data);
		byte[] result = bos.toByteArray();
		bos.flush();
		bos.close();
		return result;
	}

	/**
	 * Gzip解压
	 * @param data
	 * @return
	 */
	public static byte[] decompressByGzip(byte[] data) throws Exception{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		// 解压缩
		byte[] result = decompressByGzip(bais);;
		bais.close();
		return result;
	}

	/**
	 * 数据压缩
	 * @param os
	 * @param data
	 * @throws Exception
	 */
	public static void compressByGzip(OutputStream os, byte[] data)
			throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		gos.write(data);
		gos.close();
	}

	/**
	 * 数据解压缩
	 * @param is
	 */
	public static byte[] decompressByGzip(InputStream is)
			throws Exception{
		GZIPInputStream gis = new GZIPInputStream(is);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int count;
		byte[] data = new byte[BUFFER];
		while ((count=gis.read(data,0,data.length))>0){
			bos.write(data, 0, count);
		}
		byte[] result = bos.toByteArray();
		gis.close();
		bos.flush();
		bos.close();
		return result;
	}
}

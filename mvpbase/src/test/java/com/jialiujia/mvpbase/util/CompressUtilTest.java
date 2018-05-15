package com.jialiujia.mvpbase.util;

import android.util.Log;

import com.jialiujia.mvpbase.util.data.Convert;
import com.jialiujia.mvpbase.util.file.CompressUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MvpBase
 * Created by Administrator on 2018/2/8.
 */

public class CompressUtilTest {

	@Test public void compressByGzipTest() throws Exception {
		byte[] ordData = {(byte) 0xc6, (byte) 0xf0, (byte) 0xc0, (byte) 0xb4, (byte) 0xb0,
				(byte) 0xa1, (byte) 0xb0, (byte) 0xa1, (byte) 0xb0, (byte) 0xa1, (byte) 0xb0,
				(byte) 0xa1, (byte) 0x31, (byte) 0x31, (byte) 0x00};
		byte[] compressData = CompressUtils.compressByGzip(ordData);
		System.out.println(Convert.Byte2HexStr(compressData, false, true, false));
	}

	@Test
	public void decompressByGzipTest() throws Exception {
		byte[] comData = {0x1f, (byte) 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x75,
				(byte) 0x8d, 0x31, 0x0f,(byte)0x82,0x30,0x10,(byte)0x85,(byte)0x9d,(byte)0x9b,(byte)0xf4,
				(byte)0xff,(byte)0xcf,(byte)0xa5,(byte)0xba,0x20, 0x03,0x58,0x1b,0x09,0x0d,0x51,0x6c,
				(byte)0x81,0x62,0x23,0x16,(byte)0xa2,0x34,0x61,0x34,0x71,0x25,
				(byte)0x88,0x09,(byte)0xd1,(byte)0xc5,0x68,0x75,0x71,0x71,(byte)0xf9,(byte)0xee,
				(byte)0xbd,(byte)0xbb,(byte)0xcb,0x7b,0x59,(byte)0xbf,0x59,
				0x62,(byte)0xb4,0x60,(byte)0xc7,(byte)0xd0,(byte)0x8c,(byte)0xe9,0x3c,(byte)0x99,
				0x51,0x21,0x15,(byte)0xcf,0x31,(byte)0xf2,(byte)0xab,(byte)0xb2,
				(byte)0xe6,0x39,0x15,(byte)0x95,0x2b,(byte)0xcf,(byte)0xcd,(byte)0x93,0x14,
				(byte)0xd6, (byte) 0xe9,0x2d,0x15,0x64,0x4a, (byte) 0xde,0x57,
				0x08,0x1a, (byte) 0xcd,0x5c,0x7e,0x35,0x17,0x1a,0x27, (byte) 0xca,0x74,0x25,0x3e, (byte) 0x9c,
				(byte) 0xa8, (byte) 0xd8,0x05, (byte) 0xd1, (byte) 0x80,0x3d,0x08, (byte) 0xf8,
				0x3a,0x1c, (byte) 0xa5,0x0a,0x58,0x26,0x4d, (byte) 0xba,0x72, (byte) 0xc8,0x68, (byte) 0xba,
				(byte) 0xe8,0x46,0x45, (byte) 0xd4,0x17,0x0f, (byte) 0x84,0x21, (byte) 0xc8,0x3e, (byte) 0x8d,
				(byte) 0xbf, (byte) 0x84, (byte) 0xc0, (byte) 0xa6, (byte) 0xef, (byte) 0x87,
				(byte) 0xa6, (byte) 0xad, (byte) 0x99,0x16, (byte) 0x9f,0x04, (byte) 0xdf,(byte)0xa4,
				0x54,0x4b,0x45,0x45,(byte)0xda,(byte)0xb2,(byte)0x1e,(byte)0x7b,(byte)0xb9,
				0x1f,(byte)0xdf,(byte)0xed,(byte)0xdf,(byte)0xbf,(byte)0xfd,0x77,0x5a,0x22,(byte)0xc7,
				(byte)0xaa,(byte)0xc9,0x0b,0x15,(byte)0xce,0x5e,0x3b,
				(byte)0xd7,0x00,0x00,0x00};

		byte[] ordData = CompressUtils.decompressByGzip(comData);
		System.out.println(Convert.Byte2HexStr(ordData, false, true, false));
	}
}
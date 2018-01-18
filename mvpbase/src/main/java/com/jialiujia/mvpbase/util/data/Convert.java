package com.jialiujia.mvpbase.util.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class Convert {
	/**
	 * 字符串转换成十六进制字符串
	 */
	public static String Str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (byte b : bs) {
			bit = (b & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = b & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * byte数组转16进制字符串
	 *
	 * @param key
	 * @return
	 */
	public static String Byte2HexStr(byte[] key) {
		StringBuilder d = new StringBuilder(key.length * 2);
		for (byte aKey : key) {
			char hi = Character.forDigit((aKey >> 4) & 0x0F, 16);
			char lo = Character.forDigit(aKey & 0x0F, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}
		return d.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 *
	 * @param hex
	 * @return byte[]
	 */
	public static byte[] HexStrToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * byte数组转换成BCD码
	 * @param bcd
	 * @return
	 */
	public static String Byte2Bcd(byte[] bcd){
		StringBuilder temp = new StringBuilder(bcd.length * 2);
		for (byte aBcd : bcd) {
			temp.append((byte) ((aBcd & 0xf0) >>> 4));
			temp.append((byte) (aBcd & 0x0f));
		}
		return temp.charAt(0) == 0 ? temp.substring(1) : temp.toString();
	}

	/**
	 * 字符串转BCD码
	 * @param str
	 * @return
	 */
	public static byte[] Str2Bcd(String str){
		if (str.length() % 2 != 0) {
			str = "F" + str;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		byte[] result = baos.toByteArray();
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 数字转BCD码
	 * @param v 需要转换的数字
	 * @param len 转换后BCD码数组的长度
	 * @return
	 */
	public static byte[] Int2Bcd(int v,int len){
		byte[] ary = new byte[len];
		int temp;
		for (int j = len - 1; j >= 0; j--) {
			temp = v % 100;
			ary[j] = (byte) (((temp / 10) << 4) + ((temp % 10) & 0x0F));
			v /= 100;
		}
		return ary;
	}

	/**
	 * BCD码转数字
	 * @param ary
	 * @return
	 */
	public static int Bcd2Int(byte[] ary){
		int v = 0;
		for (byte anAry : ary) {
			v = (v * 100) + (((anAry & 0xFF) >> 4) * 10) + (anAry & 0x0F);
		}
		return v;
	}

	private static int toByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}

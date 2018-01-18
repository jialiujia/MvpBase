package com.jialiujia.mvpbase.util.data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class Digest {
	/**
	 * Hash算法 (Sun 提供)
	 * @param str
	 * @param type
	 * @return
	 */
	public static String hashEncode(String str,HashType type){
		String strResult = null;
		if (str!=null && !str.isEmpty() ){
			// SHA 加密开始
			// 创建加密对象 并傳入加密類型
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(type.toString());
				// 传入要加密的字符串
				messageDigest.update(str.getBytes("utf-8"));
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++)
				{
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1)
					{
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}

	public static enum HashType{
		MD_2("MD2",0),
		MD_5("MD5",1),
		SHA_1("SHA-1",2),
		SHA_256("SHA-256",3),
		SHA_384("SHA-384",4),
		SHA_512("SHA-512",5);

		private String value;
		private int index;

		private HashType(String value,int index){
			this.value=value;
			this.index=index;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}
}

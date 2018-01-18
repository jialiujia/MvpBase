package com.jialiujia.mvpbase.util.file;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class ImageUtils {
	public static byte[] bitmap2Bytes (Bitmap bitmap) {
		byte[] result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			result = baos.toByteArray();
			baos.flush();baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

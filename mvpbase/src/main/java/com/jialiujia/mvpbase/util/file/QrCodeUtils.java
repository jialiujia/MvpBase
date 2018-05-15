package com.jialiujia.mvpbase.util.file;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class QrCodeUtils {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 创建二维码
	 * @param density
	 * @param str
	 * @param widthAndHeight
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createQRCode(float density, String str, int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		//float scale=context.getResources().getDisplayMetrics().density;
		float scale = density;
		int widthAndHeight1=(int) (widthAndHeight * scale + 0.5f);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight1, widthAndHeight1);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 创建带图片二维码
	 * @param density
	 * @param str
	 * @param widthAndHeight
	 * @param logoBm
	 * @return
	 */
	public static Bitmap createQRCode(float density, String str, int widthAndHeight, Bitmap logoBm) {
		try {
			//配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 图像数据转换，使用了矩阵转换
			float scale = density;
			int widthAndHeight1=(int) (widthAndHeight * scale + 0.5f);
			BitMatrix matrix = new MultiFormatWriter()
					.encode(str, BarcodeFormat.QR_CODE, widthAndHeight1,
							widthAndHeight1, hints);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					} else {
						pixels[y * width + x] = WHITE;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			if (logoBm != null) {
				bitmap = addLogo(bitmap, logoBm);
			}
			//必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 在二维码中间添加Logo图案
	 * @param src
	 * @param logo
	 * @return
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		//获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		//logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

	/**
	 * 创建条形码
	 * @param density
	 * @param str
	 * @param width
	 * @param height
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createBarCode(float density,String str,int width,int height)throws WriterException{
		Hashtable<EncodeHintType, String>hints=new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		float scale=density;
		int dpwidth=(int) (width * scale + 0.5f);
		int dpheight=(int) (height * scale + 0.5f);
		BitMatrix matrix=new MultiFormatWriter().encode(str, BarcodeFormat.EAN_13, dpwidth, dpheight);
		int width1=matrix.getWidth();
		int height1=matrix.getHeight();
		int[]pixels=new int[width1*height1];

		for (int y = 0; y < height1; y++) {
			for (int x = 0; x < width1; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width1 + x] = BLACK;
				}
			}
		}
		Bitmap bitmap=Bitmap.createBitmap(width1,height1,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width1, 0, 0, width1, height1);
		return bitmap;
	}

	/**
	 * 从drawable创建Bitmap
	 * @param resources
	 * @param drawableId
	 * @return
	 */
	public static Bitmap getBitmapFromDrawable(Resources resources, int drawableId) {
		// 设置参数
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, drawableId, options);
		int height = options.outHeight;
		int width= options.outWidth;
		int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
		int minLen = Math.min(height, width); // 原图的最小边长
		if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
			float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
			inSampleSize = (int)ratio;
		}
		options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
		options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
		return BitmapFactory.decodeResource(resources, drawableId, options); // 解码文件
	}
}

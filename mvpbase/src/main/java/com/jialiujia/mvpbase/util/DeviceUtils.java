package com.jialiujia.mvpbase.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class DeviceUtils {
	/**
	 * 获取设备号(IMEI)
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getIMEI(Context context){
		TelephonyManager telephonyManager =
				(TelephonyManager)context
						.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null){
			return telephonyManager.getDeviceId();
		}
		return "";
	}

	/**
	 * 获取IMSI
	 * 返回用户唯一标识，比如GSM网络的IMSI编号 唯一的用户ID：
	 * 例如：IMSI(国际移动用户识别码) for a GSM phone.
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context){
		TelephonyManager telephonyManager =
				(TelephonyManager)context
						.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null){
			return telephonyManager.getSubscriberId();
		}
		return "";
	}

	/**
	 * 获取Android唯一ID（如果某个Andorid手机被Root过的话，
	 * 这个ID也可以被任意改变）
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context){
		String id = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		if(id != null){
			return id;
		} else {
			return "";
		}
	}
}

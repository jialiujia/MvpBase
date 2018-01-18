package com.jialiujia.mvpbase.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class NetworkUtils {
	/**
	 * 没有连接网络
	 */
	public static final int NETWORK_NONE = -1;
	/**
	 * 移动网络
	 */
	public static final int NETWORK_MOBILE = 0;
	/**
	 * 无线网络
	 */
	public static final int NETWORK_WIFI = 1;

	/**
	 * 获取当前网络状态
	 * @param context
	 * @return
	 */
	public static int getNetWorkState(Context context){
		// 得到连接管理器对象
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo!=null&&activeNetworkInfo.isConnected()){
			if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI){
				return NETWORK_WIFI;
			}
			else if(activeNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
				return NETWORK_MOBILE;
			}
		}
		else {
			return NETWORK_NONE;
		}
		return NETWORK_NONE;
	}

	/**
	 * 获取Mac地址
	 * @return
	 */
	public static byte[] getMacAddress(){
		try {
			NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
			byte[] address = networkInterface.getHardwareAddress();
			return address;
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return  new byte[]{0x00,0x00,0x00,0x00,0x00,0x00};
	}
}

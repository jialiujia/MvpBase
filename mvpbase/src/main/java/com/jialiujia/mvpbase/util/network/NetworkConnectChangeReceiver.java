package com.jialiujia.mvpbase.util.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class NetworkConnectChangeReceiver extends BroadcastReceiver {
	INetEvent event;
	IntentFilter filter;

	public NetworkConnectChangeReceiver(INetEvent event){
		this.event=event;
		filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
			int state= NetworkUtils.getNetWorkState(context);
			event.onNetChange(state);
		}
	}

	public IntentFilter getFilter() {
		return filter;
	}
}

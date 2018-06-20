package com.jialiujia.mvpbase.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jialiujia.mvpbase.R;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class FragmentA extends Fragment implements Observer {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ObservalHandler.getInstance().register(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frame_a, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ObservalHandler.getInstance().sendMessage(new Message(6, "from FragmentA"));
			}
		});
	}

	@Override
	public void onUpdate(Message message) {
		Log.i("FragmentA", (String) message.getO());
	}

	public static FragmentA newInstance() {
		
		Bundle args = new Bundle();
		
		FragmentA fragment = new FragmentA();
		fragment.setArguments(args);
		return fragment;
	}
}

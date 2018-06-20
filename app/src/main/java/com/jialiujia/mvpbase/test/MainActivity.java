package com.jialiujia.mvpbase.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jialiujia.mvpbase.R;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class MainActivity extends AppCompatActivity implements Observer {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		ObservalHandler.getInstance().register(this);
		findViewById(R.id.btn_a).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
				startActivity(intent);
			}
		});

		FragmentA fragmentA = FragmentA.newInstance();
		try {
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragmentA, R.id.frame_a);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FragmentB fragmentB = FragmentB.newInstance();
		try {
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragmentB, R.id.frame_b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdate(Message message) {
		Log.i("MainActivity", (String) message.getO());
		ObservalHandler.getInstance().unregister(this);
	}
}

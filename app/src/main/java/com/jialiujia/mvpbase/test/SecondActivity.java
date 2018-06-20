package com.jialiujia.mvpbase.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jialiujia.mvpbase.R;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class SecondActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_second);

		findViewById(R.id.btn_b).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message(100, "aaaaaa");
				ObservalHandler.getInstance().sendMessage(message);
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}

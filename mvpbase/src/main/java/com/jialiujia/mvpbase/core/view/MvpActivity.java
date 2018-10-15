package com.jialiujia.mvpbase.core.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jialiujia.mvpbase.core.activity.InputUtils;

public class MvpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            InputUtils.fixFocusedViewLeak(getApplication());
        }
    }
}

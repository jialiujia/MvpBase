package com.jialiujia.mvpbase.data;

import android.os.Build;

import java.io.Serializable;
import java.util.Objects;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public abstract class ModelBean implements Serializable {

	@Override
	public int hashCode() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return Objects.hashCode(getClass().getSimpleName());
		} else {
			return super.hashCode();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}

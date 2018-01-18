package com.jialiujia.mvpbase.core.view;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public interface BaseView<P extends BasePresenter> {
	void setPresenter(P presenter);
}

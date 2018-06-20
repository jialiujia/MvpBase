package com.jialiujia.mvpbase.core.observe;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public interface Subject {
	void registerObserver(Observer observer);

	void removeObserver(Observer observer);

	void removeAllObservers();

	void notifyObservers(Message message);
}

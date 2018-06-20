package com.jialiujia.mvpbase.test;

import android.os.Handler;

import java.util.ArrayList;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class Observal implements Subject {
	private boolean changed = false;
	private final ArrayList<Observer> observers;
	private final Handler handler;

	public Observal() {
		observers = new ArrayList<>();
		handler = new Handler();
	}

	@Override
	public synchronized void registerObserver(Observer observer) {
		if (observer == null) {
			throw new NullPointerException("The observer is null.");
		}
		synchronized (observers) {
			if (observers.contains(observer)) {
				throw new IllegalStateException("The observer has already existed.");
			}
			observers.add(observer);
		}
	}

	@Override
	public synchronized void removeObserver(Observer observer) {
		if (observer == null) {
			throw new NullPointerException("The observer is null.");
		}
		synchronized (observers) {
			int index = observers.indexOf(observer);
			if (index == -1) {
				throw new IllegalStateException("The observer was not registered");
			}
			observers.remove(index);
		}
	}

	@Override
	public synchronized void removeAllObservers() {
		synchronized (observers) {
			observers.clear();
		}
	}

	@Override
	public void notifyObservers(final Message message) {
		final Observer[] observers;

		synchronized (this) {
			if (!this.changed) {
				return;
			}
			observers = (Observer[]) this.observers.toArray(new Observer[this.observers.size()]);
			clearChanged();
		}
		for (int i = observers.length - 1; i >= 0; i--) {
			final Observer o = observers[i];
			handler.post(new Runnable() {
				@Override
				public void run() {
					o.onUpdate(message);
				}
			});
		}
	}

	public synchronized void setChanged() {
		this.changed = true;
	}

	public synchronized void clearChanged() {
		this.changed = false;
	}

	public synchronized boolean hasChanged() {
		return this.changed;
	}

	public synchronized int getObserverCount() {
		synchronized (observers) {
			return observers.size();
		}
	}
}

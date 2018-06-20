package com.jialiujia.mvpbase.test;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class ObservalHandler {
	private static volatile ObservalHandler INSTANCE;
	private final Observal observal;

	private ObservalHandler() {
		this.observal = new Observal();
	}

	public void register(Observer observer) {
		observal.registerObserver(observer);
	}

	public void unregister(Observer observer) {
		observal.removeObserver(observer);
	}

	public void clearAll() {
		observal.removeAllObservers();
	}

	public void sendMessage(Message message) {
		observal.setChanged();
		observal.notifyObservers(message);
	}

	public static ObservalHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (ObservalHandler.class) {
				if (INSTANCE == null) {
					INSTANCE = new ObservalHandler();
				}
			}
		}
		return INSTANCE;
	}
}

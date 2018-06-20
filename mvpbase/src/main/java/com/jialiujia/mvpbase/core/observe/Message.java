package com.jialiujia.mvpbase.core.observe;

/**
 * MvpBase
 * Created by Administrator on 2018/5/25.
 */

public class Message {
	private long id;
	private Object o;

	public Message(long id, Object o) {
		this.id = id;
		this.o = o;
	}

	public long getId() {
		return id;
	}

	public Object getO() {
		return o;
	}
}

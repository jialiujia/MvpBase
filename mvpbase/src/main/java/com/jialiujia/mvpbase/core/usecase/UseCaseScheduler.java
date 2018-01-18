package com.jialiujia.mvpbase.core.usecase;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

interface UseCaseScheduler {
	void execute(int taskId, Runnable runnable);

	void cancel(int taskId);

	<V extends UseCase.ResponseValue> void notifyResponse(final V response
			, final UseCase.UseCaseCallback<V> useCaseCallback);

	<V extends UseCase.ResponseValue> void onError(final int code, final String content,
	                                               final UseCase.UseCaseCallback<V> useCaseCallback);
}

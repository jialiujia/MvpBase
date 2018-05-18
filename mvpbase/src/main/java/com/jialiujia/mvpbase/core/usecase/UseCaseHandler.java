package com.jialiujia.mvpbase.core.usecase;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class UseCaseHandler {

	private static volatile UseCaseHandler INSTANCE;

	private final UseCaseScheduler mUseCaseScheduler;

	public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
		this.mUseCaseScheduler = useCaseScheduler;
	}

	public <T extends UseCase.RequestValue, R extends UseCase.ResponseValue> void execute(
			final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
		useCase.setRequestValue(values);
		useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

		mUseCaseScheduler.execute(useCase.hashCode(), new Runnable() {
			@Override
			public void run() {
				useCase.run();
			}
		});
	}

	public <T extends UseCase.RequestValue, R extends UseCase.ResponseValue> void releaseTask(final UseCase<T, R> useCase) {
		mUseCaseScheduler.cancel(useCase.hashCode());
	}

	private <V extends UseCase.ResponseValue> void notifyResponse(final V response
			, final UseCase.UseCaseCallback<V> useCaseCallback) {
		mUseCaseScheduler.notifyResponse(response, useCaseCallback);
	}

	private <V extends UseCase.ResponseValue> void notifyError(final int code, String content,
	                                                           final UseCase.UseCaseCallback<V> useCaseCallback) {
		mUseCaseScheduler.onError(code, content, useCaseCallback);
	}

	private final static class UiCallbackWrapper<V extends UseCase.ResponseValue> implements
			UseCase.UseCaseCallback<V> {

		private final UseCase.UseCaseCallback<V> mCallback;

		private final UseCaseHandler mUseCaseHandler;

		UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
		                  UseCaseHandler useCaseHandler) {
			mCallback = callback;
			mUseCaseHandler = useCaseHandler;
		}

		@Override
		public void onSuccess(V response) {
			mUseCaseHandler.notifyResponse(response, mCallback);
		}

		@Override
		public void onError(int code, String content) {
			mUseCaseHandler.notifyError(code, content, mCallback);
		}
	}

	public static UseCaseHandler getInstance(int poolSize, int maxPoolSize) {
		if (INSTANCE == null) {
			synchronized (UseCaseHandler.class){
				if (INSTANCE == null){
					INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler(poolSize, maxPoolSize));
				}
			}
		}
		return INSTANCE;
	}

	public static UseCaseHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (UseCaseHandler.class){
				if (INSTANCE == null){
					INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
				}
			}
		}
		return INSTANCE;
	}
}

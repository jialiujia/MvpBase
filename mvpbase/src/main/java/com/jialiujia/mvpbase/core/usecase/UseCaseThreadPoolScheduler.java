package com.jialiujia.mvpbase.core.usecase;

import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

class UseCaseThreadPoolScheduler implements UseCaseScheduler  {

	private static final String TAG = "UseCaseThreadPool";

	private final Handler mHandler = new Handler();

	private static final int POOL_SIZE = Runtime.getRuntime()
			.availableProcessors() + 1; //线程池初始大小

	private static final int MAX_POOL_SIZE = Runtime.getRuntime()
			.availableProcessors() * 2 + 1; //线程最大容量: 2N + 1 (N为CPU核心数)

	private static final int TIMEOUT = 2; //超时时间，超过则被回收

	private final SparseArray<Future> futureSparseArray; //存储运行线程

	private ThreadPoolExecutor mThreadPoolExecutor;

	UseCaseThreadPoolScheduler(int poolSize, int maxPoolSize) {
		init(poolSize, maxPoolSize);
		futureSparseArray = new SparseArray<>();
	}

	UseCaseThreadPoolScheduler() {
		init(POOL_SIZE, MAX_POOL_SIZE);
		futureSparseArray = new SparseArray<>();
	}

	private void init(int poolSize, int maxPoolSize) {
		mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, maxPoolSize, TIMEOUT,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		//回收空闲的核心线程
		mThreadPoolExecutor.allowCoreThreadTimeOut(true);
	}

	@Override
	public void execute(int taskId, Runnable runnable) {
		Future future = mThreadPoolExecutor.submit(runnable);
		futureSparseArray.append(taskId, future);
		Log.d(TAG, "ThreadPool Size : " + String.valueOf(mThreadPoolExecutor.getPoolSize()));
	}

	@Override
	public void cancel(int taskId) {
		Future future = futureSparseArray.get(taskId);
		if (future != null) {
			future.cancel(true);
			Log.d(TAG, "Task is canceled");
		}
		futureSparseArray.delete(taskId);
		Log.d(TAG, "ThreadPool Size : " + String.valueOf(mThreadPoolExecutor.getPoolSize()));
	}

	@Override
	public <V extends UseCase.ResponseValue> void notifyResponse(final V response, final UseCase.UseCaseCallback<V> useCaseCallback) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				useCaseCallback.onSuccess(response);
			}
		});
	}

	@Override
	public <V extends UseCase.ResponseValue> void onError(final int code, final String content, final UseCase.UseCaseCallback<V> useCaseCallback) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				useCaseCallback.onError(code, content);
			}
		});
	}
}

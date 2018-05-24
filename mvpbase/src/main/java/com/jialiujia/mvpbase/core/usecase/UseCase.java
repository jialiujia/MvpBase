package com.jialiujia.mvpbase.core.usecase;

import android.os.Build;

import java.util.Objects;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public abstract class UseCase<Q extends UseCase.RequestValue, P extends UseCase.ResponseValue> {

	private Q mRequestValue;

	private UseCaseCallback<P> mUseCaseCallback;

	public Q getRequestValue() {
		return mRequestValue;
	}

	public void setRequestValue(Q requestValue) {
		this.mRequestValue = requestValue;
	}

	public UseCaseCallback<P> getUseCaseCallback() {
		return mUseCaseCallback;
	}

	public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
		this.mUseCaseCallback = useCaseCallback;
	}

	void run() {
		executeUseCase(mRequestValue);
	}

	protected abstract void executeUseCase(Q requestValue);

	public interface UseCaseCallback<R> {
		void onSuccess(R response);

		void onError(int code, String content);
	}

	public interface RequestValue {
	}

	public interface ResponseValue {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public int hashCode() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return Objects.hash(getClass().getSimpleName());
		} else {
			return super.hashCode();
		}
	}
}

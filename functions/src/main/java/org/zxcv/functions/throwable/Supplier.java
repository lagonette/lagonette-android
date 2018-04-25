package org.zxcv.functions.throwable;

import android.support.annotation.NonNull;

public interface Supplier<T, E extends Throwable> {

	@NonNull
	static <T, E extends Throwable> Supplier<T, E> create(@NonNull T result) throws E {
		return () -> result;
	}

	@NonNull
	static <T, E extends Throwable> Supplier<T, E> throwException(@NonNull E exc) throws E {
		return () -> {
			throw exc;
		};
	}

	@NonNull
	T get() throws E;
}
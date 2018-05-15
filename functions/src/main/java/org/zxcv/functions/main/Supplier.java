package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface Supplier<R> {

	@NonNull
	static <R> Supplier<R> get(@NonNull R result) {
		return () -> result;
	}

	@NonNull
	R get();
}

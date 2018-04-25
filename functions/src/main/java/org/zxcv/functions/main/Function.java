package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface Function<R, P> {

	@NonNull
	static <R, P> Function<R, P> create(@NonNull R result) {
		return (param) -> result;
	}

	R apply(P param);
}

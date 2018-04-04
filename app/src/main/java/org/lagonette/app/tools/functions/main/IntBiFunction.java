package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface IntBiFunction<R, P2, P3> {

	@NonNull
	static <R, P2, P3> IntBiFunction create(@NonNull R result) {
		return (param1, param2, param3) -> result;
	}

	@NonNull
	R apply(int param1, @NonNull P2 param2, @NonNull P3 param3);
}

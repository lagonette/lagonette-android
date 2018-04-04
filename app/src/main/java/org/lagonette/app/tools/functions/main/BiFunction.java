package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface BiFunction<R, P1, P2> {

	@NonNull
	static <R, P1, P2> BiFunction<R, P1, P2> create(@NonNull R result) {
		return (param1, param2) -> result;
	}

	@NonNull
	R apply(@NonNull P1 param1, @NonNull P2 param2);
}

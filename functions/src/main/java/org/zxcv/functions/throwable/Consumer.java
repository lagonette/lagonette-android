package org.zxcv.functions.throwable;

import android.support.annotation.NonNull;

public interface Consumer<P, E extends Throwable> {

	static <P, E extends Throwable> void doNothing(@NonNull P param) throws E {
	}

	@NonNull
	static <P, E extends Throwable> Supplier<P, E> throwException(@NonNull E exc) throws E {
		return () -> {
			throw exc;
		};
	}

	void accept(@NonNull P param) throws E;
}
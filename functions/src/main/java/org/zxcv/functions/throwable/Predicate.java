package org.zxcv.functions.throwable;

import android.support.annotation.NonNull;

public interface Predicate<P, E extends Throwable> {

	@NonNull
	static <P, E extends Throwable> Predicate<P, E> create(boolean bool) throws E {
		return param -> bool;
	}

	@NonNull
	static <P, E extends Throwable> Supplier<P, E> throwException(@NonNull E exc) throws E {
		return () -> {
			throw exc;
		};
	}

	boolean test(@NonNull P param) throws E;
}
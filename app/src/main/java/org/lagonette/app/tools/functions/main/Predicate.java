package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface Predicate<P> {

	@NonNull
	static <P> Predicate<P> create(boolean bool) {
		return param -> bool;
	}

	boolean test(@NonNull P param);
}

package org.lagonette.app.tools.functions.main;

import android.support.annotation.Nullable;

public interface Provider<R> {

	static <R> R getNothing() {
		return null;
	}

	@Nullable
	R get();
}

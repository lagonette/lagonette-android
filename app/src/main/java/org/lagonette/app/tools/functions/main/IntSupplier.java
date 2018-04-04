package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface IntSupplier {

	@NonNull
	static IntSupplier create(int param) {
		return () -> param;
	}

	int get();
}

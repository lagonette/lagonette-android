package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface IntSupplier {

	@NonNull
	static IntSupplier create(int param) {
		return () -> param;
	}

	int get();
}

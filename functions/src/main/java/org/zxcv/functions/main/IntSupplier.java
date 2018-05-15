package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface IntSupplier {

	@NonNull
	static IntSupplier get(int param) {
		return () -> param;
	}

	int get();
}

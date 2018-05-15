package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface IntSupplier {

	//TODO Rename as get
	@NonNull
	static IntSupplier create(int param) {
		return () -> param;
	}

	int get();
}

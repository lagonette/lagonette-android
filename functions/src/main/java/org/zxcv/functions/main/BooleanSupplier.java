package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface BooleanSupplier {

	@NonNull
	static BooleanSupplier create(boolean bool) {
		return () -> bool;
	}

	boolean get();
}
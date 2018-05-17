package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface BooleanSupplier {

	static boolean getFalse() {
		return false;
	}

	static boolean getTrue() {
		return true;
	}

	@NonNull
	static BooleanSupplier get(boolean bool) {
		return () -> bool;
	}

	boolean get();
}

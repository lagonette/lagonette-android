package org.zxcv.functions.main;

import android.support.annotation.NonNull;

public interface IntBiConsumer<P2, P3> {

	static void doNothing(double param) {
	}

	void accept(int param1, @NonNull P2 param2, @NonNull P3 param3);
}

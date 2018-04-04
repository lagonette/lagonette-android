package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface QuadriConsumer<P1, P2, P3, P4> {

	static <P1, P2, P3, P4> void doNothing(
			@NonNull P1 param1,
			@NonNull P2 param2,
			@NonNull P3 param3,
			@NonNull P4 param4) {
	}

	void accept(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3, @NonNull P4 param4);
}

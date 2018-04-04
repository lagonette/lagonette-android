package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface TriConsumer<P1, P2, P3> {

	static <P1, P2, P3> void doNothing(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3) {
	}

	void accept(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3);
}

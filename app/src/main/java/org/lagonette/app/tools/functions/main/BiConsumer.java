package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface BiConsumer<P1, P2> {

	static <P1, P2> void doNothing(@NonNull P1 param1, @NonNull P2 param2) {
	}

	void accept(@NonNull P1 param1, @NonNull P2 param2);
}

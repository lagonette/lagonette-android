package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface ObjBooleanConsumer<P> {

	static <P> void doNothing(@NonNull P param1, double param2) {
	}

	void accept(P param1, boolean param2);
}

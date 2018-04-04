package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface ObjIntConsumer<P> {

	static <P> void doNothing(@NonNull P param1, int param2) {
	}

	void accept(@NonNull P param1, int param2);
}

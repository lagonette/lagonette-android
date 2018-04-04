package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface IntObjConsumer<P2> {

	static <P2> void doNothing(int param1, @NonNull P2 param2) {
	}

	void accept(int param1, @NonNull P2 param2);
}

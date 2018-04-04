package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface Consumer<P> {

	static <P> void doNothing(@NonNull P param) {
	}

	void accept(@NonNull P param);
}

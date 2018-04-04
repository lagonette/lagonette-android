package org.lagonette.app.tools.functions.main;

import android.support.annotation.NonNull;

public interface ObjBiDoubleConsumer<P> {

	static <P> void doNothing(@NonNull P param1, double param2, double param3) {
	}

	void accept(@NonNull P param1, double param2, double param3);

}

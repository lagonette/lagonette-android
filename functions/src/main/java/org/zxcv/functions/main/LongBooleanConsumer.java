package org.zxcv.functions.main;

public interface LongBooleanConsumer {

	static void doNothing(long param1, boolean param2) {
	}

	void accept(long param1, boolean param2);
}

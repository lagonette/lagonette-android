package org.zxcv.functions.main;

public interface LongConsumer {

	static void doNothing(long param) {
	}

	void accept(long param);
}

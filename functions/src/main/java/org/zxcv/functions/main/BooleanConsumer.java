package org.zxcv.functions.main;

public interface BooleanConsumer {

	static void doNothing(boolean param) {
	}

	void accept(boolean param);
}

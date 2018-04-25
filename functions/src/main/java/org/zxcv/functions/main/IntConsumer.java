package org.zxcv.functions.main;

public interface IntConsumer {

	static void doNothing(int param) {
	}

	void accept(int param);
}

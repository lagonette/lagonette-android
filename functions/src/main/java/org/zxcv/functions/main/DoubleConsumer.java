package org.zxcv.functions.main;

public interface DoubleConsumer {

	static void doNothing(double param) {
	}

	void accept(double param);
}

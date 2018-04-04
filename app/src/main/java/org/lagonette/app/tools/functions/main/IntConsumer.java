package org.lagonette.app.tools.functions.main;

public interface IntConsumer {

	static void doNothing(int param) {
	}

	void accept(int param);
}

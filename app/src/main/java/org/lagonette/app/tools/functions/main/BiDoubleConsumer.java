package org.lagonette.app.tools.functions.main;

public interface BiDoubleConsumer {

	static void doNothing(double param1, double param2) {
	}

	void accept(double param1, double param2);
}

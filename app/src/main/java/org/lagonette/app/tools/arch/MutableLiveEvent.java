package org.lagonette.app.tools.arch;

public class MutableLiveEvent<E>
		extends LiveEvent<E> {

	public void sendEvent(E event) {
		super.sendEvent(event);
	}

	public void postEvent(E event) {
		super.postEvent(event);
	}
}

package org.lagonette.app.app.arch;

public class MutableLiveEvent<E>
        extends LiveEvent<E> {

    public void postEvent(E event) {
        super.postValue(event);
    }

    public void setEvent(E event) {
        super.setValue(event);
    }
}

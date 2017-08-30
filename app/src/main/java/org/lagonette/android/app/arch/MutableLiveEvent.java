package org.lagonette.android.app.arch;

public class MutableLiveEvent<T> extends LiveEvent<T> {

    public void postEvent(T value) {
        super.postValue(value);
    }

    public void setEvent(T value) {
        super.setValue(value);
    }

    public void call() {
        setValue(null);
    }
}

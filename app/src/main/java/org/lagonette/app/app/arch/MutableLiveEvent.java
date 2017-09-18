package org.lagonette.app.app.arch;

import android.support.annotation.NonNull;

public class MutableLiveEvent<E>
        extends LiveEvent<E>
        implements EventShipper.Notifier,
        EventShipper.Poster<E>,
        EventShipper.Sender<E> {

    public void call() {
        setValue(null);
    }

    @Override
    public void send(@NonNull E event) {
        super.setValue(event);
    }

    @Override
    public void post(@NonNull E event) {
        super.postValue(event);
    }
}

package org.lagonette.app.app.arch;

import android.support.annotation.NonNull;

public interface EventShipper { //TODO delete this class

    interface Sender<E> {

        void send(@NonNull E event);
    }

    interface Poster<E> {

        void post(@NonNull E event);
    }

    interface Notifier {

        void call();
    }
}

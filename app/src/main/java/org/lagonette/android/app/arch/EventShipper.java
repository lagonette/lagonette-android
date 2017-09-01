package org.lagonette.android.app.arch;

import android.support.annotation.NonNull;

public interface EventShipper {

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

package org.lagonette.app.tools.arch;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class VoidObserver implements Observer<Void> {

    public static VoidObserver unbox(@NonNull Runnable observer) {
        return new VoidObserver(observer);
    }

    @NonNull
    private final Runnable mObserver;

    public VoidObserver(@NonNull Runnable observer) {
        mObserver = observer;
    }

    @Override
    public void onChanged(@Nullable Void aVoid) {
        mObserver.run();
    }
}

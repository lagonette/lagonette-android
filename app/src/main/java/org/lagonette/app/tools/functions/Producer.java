package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface Producer<T> {

    @NonNull
    T get();
}

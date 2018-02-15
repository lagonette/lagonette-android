package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface Supplier<T> {

    @NonNull
    T get();
}

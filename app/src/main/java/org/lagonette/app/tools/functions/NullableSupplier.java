package org.lagonette.app.tools.functions;

import android.support.annotation.Nullable;

public interface NullableSupplier<T> {

    @Nullable
    T get();
}

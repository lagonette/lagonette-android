package org.lagonette.app.tools.functions;

import android.support.annotation.Nullable;

public interface NullableProducer<T> {

    @Nullable
    T get();
}

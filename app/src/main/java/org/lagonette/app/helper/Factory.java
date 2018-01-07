package org.lagonette.app.helper;

import android.support.annotation.NonNull;

public interface Factory<T> {

    @NonNull
    T create();
}

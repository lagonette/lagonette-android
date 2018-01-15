package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface Consumer<P> {

    void accept(@NonNull P param);
}

package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface IntBiConsumer<P2, P3> {

    void accept(int param1, @NonNull P2 param2, @NonNull P3 param3);
}

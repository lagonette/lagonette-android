package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface QuadriObjConsumer<P1, P2, P3, P4> {

    void accept(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3, @NonNull P4 param4);
}

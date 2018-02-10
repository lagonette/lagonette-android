package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface BiObjConsumer<P1, P2> {

    void accept(@NonNull P1 param1, @NonNull P2 param2);
}

package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface IntObjConsumer<P2> {

    void accept(int param1, @NonNull P2 param2);
}

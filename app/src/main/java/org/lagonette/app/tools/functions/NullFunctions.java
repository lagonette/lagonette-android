package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface NullFunctions {

    static <P> void accept(@NonNull P param) {}

    static void accept(int param) {}

    static void accept(long param) {}

    static void run() {}
}

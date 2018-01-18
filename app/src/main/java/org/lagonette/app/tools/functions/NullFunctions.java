package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface NullFunctions {

    static <P> void doNothing(@NonNull P param) {}

    static void doNothing(int param) {}

    static void doNothing(long param) {}

    static void doNothing(double param) {}

    static void doNothing(double param1, double param2) {}

    static void doNothing() {}
}

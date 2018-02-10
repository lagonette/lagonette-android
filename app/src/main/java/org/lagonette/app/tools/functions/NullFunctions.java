package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface NullFunctions {

    static <P> void doNothing(@NonNull P param) {}

    static <P2, P3> void doNothing(int param1, @NonNull P2 param2, @NonNull P3 param3) {}

    static <P1, P2> void doNothing(@NonNull P1 param1, @NonNull P2 param2) {}

    static <P1, P2, P3, P4> void doNothing(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3, @NonNull P4 param4) {}

    static <P> void doNothing(int param1, @NonNull P param2) {}

    static void doNothing(int param) {}

    static void doNothing(long param) {}

    static void doNothing(double param) {}

    static void doNothing(double param1, double param2) {}

    static void doNothing() {}

    static <P> boolean falsePredicate(@NonNull P param) {
        return false;
    }
}

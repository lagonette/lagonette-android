package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;

public interface NullFunctions {

    static <P> void doNothing(@NonNull P param) {}

    static <P1> void doNothing(@NonNull P1 param1, int param2) {}

    static <P2, P3> void doNothing(int param1, @NonNull P2 param2, @NonNull P3 param3) {}

    static <P1, P2> void doNothing(@NonNull P1 param1, @NonNull P2 param2) {}

    static <P1, P2, P3, P4> void doNothing(@NonNull P1 param1, @NonNull P2 param2, @NonNull P3 param3, @NonNull P4 param4) {}

    static <P> void doNothing(int param1, @NonNull P param2) {}

    static void doNothing(int param) {}

    static void doNothing(long param) {}

    static void doNothing(double param) {}

    static void doNothing(double param1, double param2) {}

    static <P> void doNothing(@NonNull P param1, double param2, double param3) {}

    static void doNothing() {}

    static <P> boolean falsePredicate(@NonNull P param) {
        return false;
    }

    static <P> Predicate<P> predicate(boolean bool) { return paran -> bool; }

    static BooleanSupplier supply(boolean bool) { return () -> bool; }

    static <P> Supplier<P> supply(@NonNull P param) { return () -> param; }
}

package org.lagonette.app.tools.functions;

import android.support.annotation.NonNull;
import android.util.Log;

public class Logger {

    public static <R, P> Function<R, P> log(@NonNull String tag, @NonNull String message, Function<R, P> function) {
        return param -> {
            Log.d(tag, message);
            return function.apply(param);
        };
    }

    public static <P> Consumer<P> log(@NonNull String tag, @NonNull String message, Consumer<P> consumer) {
        return param -> {
            Log.d(tag, message);
            consumer.accept(param);
        };
    }

    public static LongConsumer log2(@NonNull String tag, @NonNull String message, LongConsumer consumer) {
        return param -> {
            Log.d(tag, message);
            consumer.accept(param);
        };
    }

    public static Runnable log(@NonNull String tag, @NonNull String message, Runnable runnable) {
        return () -> {
            Log.d(tag, message);
            runnable.run();
        };
    }
}

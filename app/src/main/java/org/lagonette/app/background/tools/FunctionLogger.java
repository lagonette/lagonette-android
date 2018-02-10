package org.lagonette.app.background.tools;

import android.support.annotation.NonNull;
import android.util.Log;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.tools.functions.Consumer;

public class FunctionLogger {

    public static Runnable logd(@NonNull String tag, @NonNull String message, @NonNull Runnable runnable) {
        return () -> {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message);
            }
            runnable.run();
        };
    }

    public static <P> Consumer<P> logd(@NonNull String tag, @NonNull String message, @NonNull Consumer<P> consumer) {
        return param -> {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message);
            }
            consumer.accept(param);
        };
    }
}

package org.lagonette.app.background.tools;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.Function;
import org.lagonette.app.tools.functions.IntBiObjConsumer;
import org.lagonette.app.tools.functions.IntBiObjFunction;
import org.lagonette.app.tools.functions.Producer;

public class ErrorCatcher {

    @NonNull
    public static <P> Consumer<P> logError(@NonNull Function<String, P> message) {
        return param -> FirebaseCrash.logcat(Log.ERROR, TAG, message.apply(param));
    }

    @NonNull
    public static <P2, P3> IntBiObjConsumer<P2, P3> logError(@NonNull IntBiObjFunction<String, P2, P3> message) {
        return (param1, param2, param3) -> FirebaseCrash.logcat(Log.ERROR, TAG, message.apply(param1, param2, param3));
    }

    @NonNull
    public static Runnable logError(@NonNull Producer<String> message) {
        return () -> FirebaseCrash.logcat(Log.ERROR, TAG, message.get());
    }

    private static final String TAG = "ErrorCatcher";

    public static <E extends Exception> void catchException(@NonNull E e) {
        Log.e(TAG, "catchError: ", e);
        FirebaseCrash.report(e);
    }

}

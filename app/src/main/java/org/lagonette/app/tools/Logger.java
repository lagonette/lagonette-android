package org.lagonette.app.tools;

import android.support.annotation.NonNull;
import android.util.Log;

import org.zxcv.functions.main.Consumer;
import org.zxcv.functions.main.Function;
import org.zxcv.functions.main.LongConsumer;
import org.zxcv.functions.main.Runnable;

public class Logger {

	public static <R, P> Function<R, P> log(
			@NonNull String tag,
			@NonNull String message,
			Function<R, P> function) {
		return param -> {
			Log.d(tag, message);
			return function.apply(param);
		};
	}

	public static <P> Consumer<P> log(
			@NonNull String tag,
			@NonNull String message,
			Consumer<P> consumer) {
		return param -> {
			Log.d(tag, message);
			consumer.accept(param);
		};
	}

	public static LongConsumer log(
			@NonNull String tag,
			@NonNull String message,
			LongConsumer consumer) {
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

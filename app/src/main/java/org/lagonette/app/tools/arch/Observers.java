package org.lagonette.app.tools.arch;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import org.lagonette.app.tools.functions.main.Predicate;
import org.lagonette.app.tools.functions.main.Supplier;

public final class Observers {

	public interface NotNullObserver<T> {

		void onChanged(@NonNull T t);
	}

	private Observers() {
	}

	public static <T> Observer<T> ensure(
			@NonNull Supplier<T> failSafe,
			@NonNull NotNullObserver<T> observer) {
		return value -> observer.onChanged(
				value == null
						? failSafe.get()
						: value
		);
	}

	public static <T> Observer<T> ensure(
			@NonNull Predicate<T> predicate,
			@NonNull Supplier<T> failSafe,
			@NonNull NotNullObserver<T> observer) {
		return value -> observer.onChanged(
				predicate.test(value)
						? value
						: failSafe.get()
		);
	}

}

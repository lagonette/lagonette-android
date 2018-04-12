package org.lagonette.app.tools.arch;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.tools.functions.main.LongConsumer;

public class LongObserver
		implements Observer<Long> {

	@NonNull
	private final LongConsumer mObserver;

	private final long mDefaultValue;

	public LongObserver(long defaultValue, @NonNull LongConsumer observer) {
		mDefaultValue = defaultValue;
		mObserver = observer;
	}

	public LongObserver(@NonNull LongConsumer observer) {
		this(0, observer);
	}

	public static LongObserver unbox(long defaultValue, @NonNull LongConsumer observer) {
		return new LongObserver(defaultValue, observer);
	}

	@Override
	public void onChanged(@Nullable Long objValue) {
		mObserver.accept(objValue != null ? objValue : mDefaultValue);
	}
}

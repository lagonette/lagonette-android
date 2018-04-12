package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.tools.arch.LiveEventBus;
import org.lagonette.app.tools.arch.LiveEventBus.Event;
import org.lagonette.app.tools.arch.LiveEventBus.VoidObserver;

public class LiveEventBusViewModel
		extends ViewModel {

	private final LiveEventBus mLiveEventBus;

	public LiveEventBusViewModel() {
		mLiveEventBus = new LiveEventBus();
	}

	public <Payload> void publish(@NonNull Event<Payload> event, @Nullable Payload payload) {
		mLiveEventBus.publish(event, payload);
	}

	public void publish(@NonNull Event<Void> event) {
		mLiveEventBus.publish(event);
	}

	public <Payload> void subscribe(
			@NonNull Event<Payload> event,
			@NonNull LifecycleOwner owner,
			@NonNull Observer<Payload> observer) {
		mLiveEventBus.subscribe(event, owner, observer);
	}

	public void subscribe(
			@NonNull Event<Void> event,
			@NonNull LifecycleOwner owner,
			@NonNull VoidObserver observer) {
		mLiveEventBus.subscribe(event, owner, observer);
	}

	public <T> void unregister(@NonNull Event<T> event, @NonNull LifecycleOwner owner) {
		mLiveEventBus.unregister(event, owner);
	}
}

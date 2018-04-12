package org.lagonette.app.tools.arch;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

public class LiveEventBus {

	public static class Event<Payload> {

	}

	public interface VoidObserver {

		void onChanged();
	}

	private final ArrayMap<Event<?>, MutableLiveEvent<Object>> mLiveEvents;

	public LiveEventBus() {
		mLiveEvents = new ArrayMap<>();
	}

	private <T> MutableLiveEvent<Object> get(@NonNull Event<T> event) {
		if (!mLiveEvents.containsKey(event)) {
			MutableLiveEvent<Object> liveEvent = new MutableLiveEvent<>();
			mLiveEvents.put(event, liveEvent);
			return liveEvent;
		}
		else {
			return mLiveEvents.get(event);
		}
	}

	public <Payload> void publish(@NonNull Event<Payload> event, @Nullable Payload payload) {
		get(event).sendEvent(payload);
	}

	public <Payload> void publish(@NonNull Event<Payload> event) {
		get(event).sendEvent(null);
	}

	public <Payload> void subscribe(
			@NonNull Event<Payload> event,
			@NonNull LifecycleOwner owner,
			@NonNull Observer<Payload> observer) {
		get(event).observe(
				owner,
				payload -> observer.onChanged((Payload) payload)
		);
	}

	public void subscribe(
			@NonNull Event<Void> event,
			@NonNull LifecycleOwner owner,
			@NonNull VoidObserver observer) {
		get(event).observe(
				owner,
				payload -> observer.onChanged()
		);
	}

	public <T> void unregister(@NonNull Event<T> event, @NonNull LifecycleOwner owner) {
		if (mLiveEvents.containsKey(event)) {
			mLiveEvents.get(event).removeObservers(owner);
			mLiveEvents.remove(event);
		}
	}

}

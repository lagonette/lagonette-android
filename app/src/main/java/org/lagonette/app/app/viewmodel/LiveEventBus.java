package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import org.lagonette.app.app.arch.MutableLiveEvent;

public class LiveEventBus extends ViewModel {

    public static class Event<Payload> {}

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
        get(event).setEvent(payload);
    }

    public <Payload> void subscribe(@NonNull Event<Payload> event, @NonNull LifecycleOwner owner, @NonNull Observer<Payload> observer) {
        //TODO What is happening when there are several observers ?
        get(event).observe(
                owner,
                payload -> observer.onChanged((Payload) payload)
        );
    }

    public <T> void unregister(@NonNull Event<T> event, @NonNull LifecycleOwner owner) {
        if (mLiveEvents.containsKey(event)) {
            mLiveEvents.get(event).removeObservers(owner);
            mLiveEvents.remove(event);
        }
    }

}

package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.arch.LiveEventBus;

public class LiveEventBusViewModel extends ViewModel {

    private final LiveEventBus mLiveEventBus;

    public LiveEventBusViewModel() {
        mLiveEventBus = new LiveEventBus();
    }


    public <Payload> void publish(@NonNull LiveEventBus.Event<Payload> event, @Nullable Payload payload) {
        mLiveEventBus.publish(event, payload);
    }

    public <Payload> void subscribe(@NonNull LiveEventBus.Event<Payload> event, @NonNull LifecycleOwner owner, @NonNull Observer<Payload> observer) {
        mLiveEventBus.subscribe(event, owner, observer);
    }

    public <T> void unregister(@NonNull LiveEventBus.Event<T> event, @NonNull LifecycleOwner owner) {
        mLiveEventBus.unregister(event, owner);
    }
}

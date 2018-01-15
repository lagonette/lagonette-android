package org.lagonette.app.tools.arch;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class LiveEvent<T> extends LiveData<T> {

    public static class WrappedObserver<T> implements Observer<T> {

        @NonNull
        private final AtomicBoolean mPending;

        @NonNull
        private final Observer<T> mObserver;

        public WrappedObserver(@NonNull Observer<T> observer) {
            mObserver = observer;
            mPending = new AtomicBoolean(false);
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (mPending.compareAndSet(true, false)) {
                mObserver.onChanged(t);
            }
        }

        public void pending() {
            mPending.set(true);
        }
    }

    @NonNull
    private final ArrayMap<Observer<T>, WrappedObserver<T>> mWrappedObservers = new ArrayMap<>();

    @NonNull
    private final ArrayMap<Observer<T>, LifecycleOwner> mLifecycleOwners = new ArrayMap<>();

    @Override
    public void observe(LifecycleOwner owner, Observer<T> observer) {
        WrappedObserver<T> wrappedObserver = new WrappedObserver<>(observer);
        mLifecycleOwners.put(observer, owner);
        mWrappedObservers.put(observer, wrappedObserver);
        super.observe(owner, wrappedObserver);
    }

    @Override
    public void observeForever(Observer<T> observer) {
        WrappedObserver<T> wrappedObserver = new WrappedObserver<>(observer);
        mWrappedObservers.put(observer, wrappedObserver);
        super.observeForever(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        mLifecycleOwners.remove(observer);
        WrappedObserver<T> wrappedObserver = mWrappedObservers.remove(observer);

        super.removeObserver(wrappedObserver);
    }

    @Override
    public void removeObservers(LifecycleOwner owner) {
        ArrayList<Observer<T>> list = new ArrayList<>();
        for (Map.Entry<Observer<T>, LifecycleOwner> entry : mLifecycleOwners.entrySet()) {
            if (entry.getValue() == owner) {
                list.add(entry.getKey());
            }
        }
        mWrappedObservers.removeAll(list);
        mLifecycleOwners.removeAll(list);

        super.removeObservers(owner);
    }

    @Override
    protected void setValue(T value) {
        for (WrappedObserver<T> observer : mWrappedObservers.values()) {
            observer.pending();
        }
        super.setValue(value);
    }

    protected void sendEvent(T value) {
        setValue(value);
    }

    protected void postEvent(T value) {
        super.postValue(value);
    }

}
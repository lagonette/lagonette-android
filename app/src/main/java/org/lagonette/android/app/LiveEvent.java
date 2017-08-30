package org.lagonette.android.app;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.v4.util.ArrayMap;

import java.util.concurrent.atomic.AtomicBoolean;

public class LiveEvent<T> extends LiveData<T> {

    private AtomicBoolean mPending = new AtomicBoolean(false);

    private ArrayMap<Observer<T>, Observer<T>> mObservers = new ArrayMap<>();

    @Override
    public void observe(LifecycleOwner owner, Observer<T> observer) {
        Observer<T> wrappedObserver = it -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it);
            }
        };
        mObservers.put(observer, wrappedObserver);
        super.observe(owner, wrappedObserver);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        super.removeObserver(mObservers.get(observer));
    }

    @Override
    protected void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

}
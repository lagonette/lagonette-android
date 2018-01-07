package org.lagonette.app.app.arch;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

//TODO Use it
public abstract class NullSafeGuard<T> {

    public interface Factory<T> {

        T ensure (T value);
    }

    public interface NullSafeObserver<T> {

        void onChanged(@NonNull T t);
    }

    public static class WrappedObserver<T> implements Observer<T> {

        @NonNull
        private final NullSafeObserver<T> mObserver;

        @NonNull
        private final NullSafeGuard.Factory<T> mFactory;

        public WrappedObserver(@NonNull NullSafeGuard.Factory<T> factory, @NonNull NullSafeObserver<T> observer) {
            mObserver = observer;
            mFactory = factory;
        }

        @Override
        public void onChanged(@Nullable T value) {
            mObserver.onChanged(mFactory.ensure(value));
        }
    }

    @NonNull
    public Observer<T> guard(@NonNull NullSafeObserver<T> observer) {
        return new WrappedObserver<>(
                this::ensure,
                observer
        );
    }

    @NonNull
    protected abstract T ensure(@Nullable T value);
}

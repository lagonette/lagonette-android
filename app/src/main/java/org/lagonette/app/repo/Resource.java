package org.lagonette.app.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.worker.WorkerState;

public class Resource<T> {

    public static <T> Resource<T> create(@NonNull WorkerState state, @Nullable T data) {
        return new Resource<>(state, data);
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(WorkerState.success(), data);
    }

    public static <T> Resource<T> error(@NonNull Error error, @Nullable T data) {
        return new Resource<>(WorkerState.error(error), data);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(WorkerState.loading(), data);
    }

    @NonNull
    public final WorkerState state;

    @NonNull
    public final T data;

    private Resource(@NonNull WorkerState state, @Nullable T data) {
        this.state = state;
        this.data = data;
    }

}

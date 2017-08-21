package org.lagonette.android.repo;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Resource<T> {

    public static final int LOADING = 0;

    public static final int SUCCESS = 1;

    public static final int ERROR = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            LOADING,
            SUCCESS,
            ERROR
    })
    public @interface Status {
    }

    @Status
    public final int status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    private Resource(@Status int status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}

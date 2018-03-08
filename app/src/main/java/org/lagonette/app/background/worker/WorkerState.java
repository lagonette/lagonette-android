package org.lagonette.app.background.worker;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.error.Error;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WorkerState {

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

    public static WorkerState loading() {
        return new WorkerState(LOADING);
    }

    public static WorkerState error(@NonNull Error error) {
        return new WorkerState(ERROR, error);
    }

    public static WorkerState success() {
        return new WorkerState(SUCCESS);
    }

    @Status
    public final int status;

    @Nullable
    public final Error error;

    private WorkerState(@Status int status, @NonNull Error error) {
        this.status = status;
        this.error = error;
    }

    public WorkerState(@Status int status) {
        this.status = status;
        this.error = null;
    }

    public boolean isSuccessful() {
        return status == SUCCESS;
    }

}

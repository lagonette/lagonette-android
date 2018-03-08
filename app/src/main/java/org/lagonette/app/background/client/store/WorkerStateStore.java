package org.lagonette.app.background.client.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.worker.WorkerState;

public class WorkerStateStore {

    @Nullable
    private WorkerState mState;

    public void store(@NonNull WorkerState state) {
        mState = state;
    }

    @NonNull
    public WorkerState getState() {
        return mState != null ? mState : WorkerState.error(Error.UNKNOWN_ERROR);
    }
}

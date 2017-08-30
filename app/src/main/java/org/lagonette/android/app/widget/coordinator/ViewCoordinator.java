package org.lagonette.android.app.widget.coordinator;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

public class ViewCoordinator {

    @NonNull
    private final Context mContext;

    public ViewCoordinator(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    protected Context getContext() {
        return mContext;
    }

    protected Resources getResources() {
        return mContext.getResources();
    }
}

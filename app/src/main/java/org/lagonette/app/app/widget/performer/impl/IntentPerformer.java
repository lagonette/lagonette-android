package org.lagonette.app.app.widget.performer.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.util.IntentUtils;

public class IntentPerformer {

    @NonNull
    private final Context mContext;

    public IntentPerformer(@NonNull Context context) {
        mContext = context;
    }

    public void startDirection(double latitude, double longitude, @Nullable Runnable onError) {
        boolean success = IntentUtils.startDirection(
                mContext,
                latitude,
                longitude
        );
        if (!success) {
            onError.run();
        }
    }

    public void makeCall(@NonNull String phoneNumber, @Nullable Runnable onError) {
        boolean success = IntentUtils.makeCall(
                mContext,
                phoneNumber
        );
        if (!success && onError != null) {
            onError.run();
        }
    }

    public void goToWebsite(@NonNull String url, @Nullable Runnable onError) {
        boolean success = IntentUtils.goToWebsite(
                mContext,
                url
        );
        if (!success && onError != null) {
            onError.run();
        }
    }

    public void writeEmail(@NonNull String email, @Nullable Runnable onError) {
        boolean success = IntentUtils.writeEmail(
                mContext,
                email
        );
        if (!success && onError != null) {
            onError.run();
        }
    }
}

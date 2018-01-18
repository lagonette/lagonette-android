package org.lagonette.app.app.widget.performer.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.util.IntentUtils;

public class IntentPerformer {

    @NonNull
    private final Context mContext;

    @NonNull
    public Consumer<Error> onError = NullFunctions::doNothing;

    public IntentPerformer(@NonNull Context context) {
        mContext = context;
    }

    public void startDirection(double latitude, double longitude) {
        boolean success = IntentUtils.startDirection(
                mContext,
                latitude,
                longitude
        );
        if (!success) {
            onError.accept(Error.NoDirectionAppFound);
        }
    }

    public void makeCall(@NonNull String phoneNumber) {
        boolean success = IntentUtils.makeCall(
                mContext,
                phoneNumber
        );
        if (!success) {
            onError.accept(Error.NoCallAppFound);
        }
    }

    public void goToWebsite(@NonNull String url) {
        boolean success = IntentUtils.goToWebsite(
                mContext,
                url
        );
        if (!success) {
            onError.accept(Error.NoBrowserAppFound);
        }
    }

    public void writeEmail(@NonNull String email) {
        boolean success = IntentUtils.writeEmail(
                mContext,
                email
        );
        if (!success) {
            onError.accept(Error.NoEmailAppFound);
        }
    }
}

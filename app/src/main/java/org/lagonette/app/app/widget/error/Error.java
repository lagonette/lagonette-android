package org.lagonette.app.app.widget.error;


import android.support.annotation.StringRes;

import org.lagonette.app.R;

public enum Error {

    NoDirectionAppFound(R.string.error_no_direction_app_found),
    NoCallAppFound(R.string.error_no_call_app_found),
    NoEmailAppFound(R.string.error_no_email_app_found),
    NoBrowserAppFound(R.string.error_no_browser_app_found);

    @StringRes
    public final int stringRes;

    Error(@StringRes int stringRes) {
        this.stringRes = stringRes;
    }
}
package org.lagonette.app.app.widget.error;


import android.support.annotation.StringRes;

import org.lagonette.app.R;

public enum Error {

    NO_DIRECTION_APP_FOUND(R.string.error_no_direction_app_found),

    NO_CALL_APP_FOUND(R.string.error_no_call_app_found),

    NO_EMAIL_APP_FOUND(R.string.error_no_email_app_found),

    NO_BROWSER_APP_FOUND(R.string.error_no_browser_app_found),

    PARTNER_NOT_LOADED(R.string.error_getting_partners_first),

    PARTNER_NOT_UPDATED(R.string.error_getting_partners);

    @StringRes
    public final int stringRes;

    Error(@StringRes int stringRes) {
        this.stringRes = stringRes;
    }
}
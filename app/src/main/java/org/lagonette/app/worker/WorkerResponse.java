package org.lagonette.app.worker;

import android.support.annotation.Nullable;

public class WorkerResponse {

    // TODO make and use this class correctly
    // TODO Maybe make a Message class with toString(Context) method

    private boolean mIsSuccessful = false;

    @Nullable
    private String mErrorMessage;

    public void setIsSuccessful(boolean isSuccessful) {
        mIsSuccessful = isSuccessful;
    }

    public void setErrorMessage(@Nullable String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return mIsSuccessful;
    }

    @Nullable
    public String getErrorMessage(){
        return mErrorMessage;
    }

}

package org.lagonette.app.api.client;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.response.ApiResponse;

import okhttp3.ResponseBody;

public abstract class EntityClient<R extends ApiResponse>
        extends RetrofitClient<R> {

    private static final String TAG = "EntityClient";

    @Override
    protected void onSuccessfulResponse(int code, @NonNull R body) throws ApiClientException {
        if (TextUtils.isEmpty(body.errors)) {
            onSuccessfulBody(body);
        }
        else {
            onErrorBody(body.errors);
        }
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
        throw new ApiClientException("Response is not successful!");
    }

    protected void onErrorBody(@NonNull String errors) throws ApiClientException {
        FirebaseCrash.logcat(Log.ERROR, TAG, "Errors: " + errors);
        throw new ApiClientException("Response contains errors: " + errors);
    }

    protected abstract void onSuccessfulBody(@NonNull R body) throws ApiClientException;

}

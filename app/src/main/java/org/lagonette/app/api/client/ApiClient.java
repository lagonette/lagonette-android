package org.lagonette.app.api.client;

import android.support.annotation.NonNull;
import android.util.Log;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.service.LaGonetteService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public abstract class ApiClient<T> {

    private static final String TAG = "ApiClient";

    private boolean mIsSuccess = false;

    public void call() throws ApiClientException {
        try {
            mIsSuccess = false;
            Call<T> call = createCall();
            if (BuildConfig.DEBUG) logCall();
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                onSuccessfulResponse(response.code(), response.body());
                mIsSuccess = true;
            } else {
                onErrorResponse(response.code(), response.message(), response.errorBody());
            }
        } catch (IOException e) {
            throw new ApiClientException(e);
        }
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    // TODO Make retrofit log in debug
    private void logCall() {
        Log.d(TAG, "Call " + LaGonetteService.HOST + getEndpoint());
    }

    protected abstract String getEndpoint();

    protected abstract Call<T> createCall();

    protected abstract void onSuccessfulResponse(int code, @NonNull T body) throws ApiClientException;

    protected abstract void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException;
}

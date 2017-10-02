package org.lagonette.app.api.client;

import android.support.annotation.NonNull;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.client.exception.ApiClientException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public abstract class ApiClient<T> {

    public void call() throws ApiClientException {
        try {
            Call<T> call = createCall();
            if (BuildConfig.DEBUG) logCall();
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                onSuccessfulResponse(response.code(), response.body());
            } else {
                onErrorResponse(response.code(), response.message(), response.errorBody());
            }
        } catch (IOException e) {
            throw new ApiClientException(e);
        }
    }

    protected abstract void logCall();

    protected abstract Call<T> createCall();

    protected abstract void onSuccessfulResponse(int code, @NonNull T body) throws ApiClientException;

    protected abstract void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException;
}

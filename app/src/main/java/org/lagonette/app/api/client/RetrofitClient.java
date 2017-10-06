package org.lagonette.app.api.client;

import android.support.annotation.NonNull;

import org.lagonette.app.api.client.exception.ApiClientException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public abstract class RetrofitClient<R> {

    private boolean mIsSuccess = false;

    public void call() throws ApiClientException {
        try {
            mIsSuccess = false;
            Call<R> call = createCall();
            Response<R> response = call.execute();

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

    @NonNull
    protected abstract Call<R> createCall();

    protected abstract void onSuccessfulResponse(int code, @NonNull R body) throws ApiClientException;

    protected abstract void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException;
}

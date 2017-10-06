package org.lagonette.app.api.client;

import android.support.annotation.NonNull;

import org.lagonette.app.api.client.exception.ApiClientException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class LambdaApiClient<R>
        extends RetrofitClient<R> {

    public interface CallFactory<R> {
        Call<R> create();
    }

    public interface OnSuccessfulCallback<R> {
        void onSuccessfulResponse(int code, @NonNull R body) throws ApiClientException;
    }

    public interface OnErrorCallback {
        void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException;
    }

    @NonNull
    private final CallFactory<R> mCallFactory;

    @NonNull
    private final OnSuccessfulCallback<R> mOnSuccessfulCallback;

    @NonNull
    private final OnErrorCallback mOnErrorCallback;

    public LambdaApiClient(
            @NonNull CallFactory<R> callFactory,
            @NonNull OnSuccessfulCallback<R> onSuccessfulCallback,
            @NonNull OnErrorCallback onErrorCallback) {
        mCallFactory = callFactory;
        mOnSuccessfulCallback = onSuccessfulCallback;
        mOnErrorCallback = onErrorCallback;
    }

    @Override
    @NonNull
    protected Call<R> createCall() {
        return mCallFactory.create();
    }

    @Override
    protected void onSuccessfulResponse(int code, @NonNull R body) throws ApiClientException {
        mOnSuccessfulCallback.onSuccessfulResponse(code, body);
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        mOnErrorCallback.onErrorResponse(code, message, errorBody);
    }
}

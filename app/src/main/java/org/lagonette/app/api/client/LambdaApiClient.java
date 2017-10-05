package org.lagonette.app.api.client;

import android.support.annotation.NonNull;

import org.lagonette.app.api.client.exception.ApiClientException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class LambdaApiClient<T>
        extends ApiClient<T> {

    public interface CallFactory<T> {
        Call<T> create();
    }

    public interface OnSuccessfulCallback<T> {
        void onSuccessfulResponse(int code, @NonNull T body) throws ApiClientException;
    }

    public interface OnErrorCallback {
        void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException;
    }

    private final String mEndpoint;

    private final CallFactory<T> mCallFactory;

    private final OnSuccessfulCallback<T> mOnSuccessfulCallback;

    private final OnErrorCallback mOnErrorCallback;

    public LambdaApiClient(
            @NonNull String endpoint,
            @NonNull CallFactory<T> callFactory,
            @NonNull OnSuccessfulCallback<T> onSuccessfulCallback,
            @NonNull OnErrorCallback onErrorCallback) {
        mEndpoint = endpoint;
        mCallFactory = callFactory;
        mOnSuccessfulCallback = onSuccessfulCallback;
        mOnErrorCallback = onErrorCallback;
    }

    @Override
    protected String getEndpoint() {
        return mEndpoint;
    }

    @Override
    protected Call<T> createCall() {
        return mCallFactory.create();
    }

    @Override
    protected void onSuccessfulResponse(int code, @NonNull T body) throws ApiClientException {
        mOnSuccessfulCallback.onSuccessfulResponse(code, body);
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        mOnErrorCallback.onErrorResponse(code, message, errorBody);
    }
}

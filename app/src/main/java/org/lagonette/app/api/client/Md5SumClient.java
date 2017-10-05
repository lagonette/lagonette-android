package org.lagonette.app.api.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.response.Md5SumResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class Md5SumClient extends ApiClient<Md5SumResponse> {

    private static final String TAG = "Md5SumClient";

    public interface CallFactory {

        @Nullable
        Call<Md5SumResponse> create();
    }

    public interface LocalMd5SumRetriever {

        @Nullable
        String retrieve();
    }

    @NonNull
    private final CallFactory mCallFactory;

    @NonNull
    private final LocalMd5SumRetriever mRetriever;

    @NonNull
    private final String mEndpoint;

    private boolean mIsMd5SumChanged;

    public Md5SumClient(
            @NonNull String endpoint,
            @NonNull CallFactory callFactory,
            @NonNull LocalMd5SumRetriever retriever) {
        mEndpoint = endpoint;
        mCallFactory = callFactory;
        mRetriever = retriever;
    }

    @Override
    protected String getEndpoint() {
        return mEndpoint;
    }

    @Override
    protected Call<Md5SumResponse> createCall() {
        return mCallFactory.create();
    }

    @Override
    protected void onSuccessfulResponse(int code, @NonNull Md5SumResponse body) throws ApiClientException {
        String localMd5Sum = mRetriever.retrieve();
        String remoteMd5Sum = body.md5Sum;
        if (remoteMd5Sum == null) {
            throw new ApiClientException("remote MD5 sum is NULL");
        } else {
            mIsMd5SumChanged = localMd5Sum == null || !localMd5Sum.equals(remoteMd5Sum);
        }
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
        throw new ApiClientException("Response is not successful!");
    }

    public boolean isMd5SumChanged() {
        return mIsMd5SumChanged;
    }
}

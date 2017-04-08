package com.gonette.android.content.loader.base;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.gonette.android.BuildConfig;

public abstract class BundleLoader extends AsyncTaskLoader<Bundle> implements LoaderStatus {

    private static final String TAG = "BundleLoader";

    public static final String ARG_STATUS = "arg:status";

    @NonNull
    protected final Bundle mBundle;

    @NonNull
    protected final Context mContext;

    @Nullable
    protected Handler mHandler;

    protected boolean mLoadInProgress = false;

    public BundleLoader(@NonNull Context context, @Nullable Bundle bundle) {
        super(context);
        mContext = context;
        if (bundle == null) {
            mBundle = new Bundle();
        } else {
            if (bundle.containsKey(ARG_STATUS)) {
                throw new IllegalArgumentException(
                        "Bundle can't contain arg:status value at the beginning."
                );
            }
            mBundle = bundle;
        }
    }

    @Override
    public void deliverResult(Bundle data) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            Log.d(TAG, "deliverResult() " + mBundle.toString() + " " + toString());
            super.deliverResult(data);
        }
    }

    public int getStatus() {
        return mBundle.getInt(ARG_STATUS, STATUS_ERROR);
    }

    @Override
    protected Bundle onLoadInBackground() {
        mLoadInProgress = true;
        if (mBundle.size() > 0) {
            readArguments(mBundle);
            mBundle.clear();
        }
        if (mBundle.equals(loadInBackground())) {
            mLoadInProgress = false;
            if (!mBundle.containsKey(ARG_STATUS)) {
                if (BuildConfig.DEBUG) {
                    throw new IllegalArgumentException(
                            "mBundle must contain arg:status value at the end."
                    );
                } else {
//                    CrashReportingUtils.logException(
//                            new Exception(
//                                    "mBundle must contain arg:status value at the end."
//                            )
//                    );
                    // TODO Firebase
                    mBundle.putInt(ARG_STATUS, STATUS_ERROR);
                }
            }
            if (mHandler != null) {
                mHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                deliverResult(mBundle);
                            }
                        }
                );
                mHandler = null;
            }
            return mBundle;
        }
        mLoadInProgress = false;
        throw new IllegalArgumentException("Do not create a new Bundle, use mBundle instead.");
    }

    @Override
    protected void onReset() {
        onStopLoading();
    }

    @Override
    protected void onStartLoading() {
        if (mBundle.containsKey(ARG_STATUS)) {
            deliverResult(mBundle);
        }
        if (takeContentChanged() || !mBundle.containsKey(ARG_STATUS)) {
            // onLoadInBackground() is called twice while the Loader is being re-initialized
            // (after an orientation change or when resuming the app)
            // before loadInBackground() returns
            if (mLoadInProgress) {
                mHandler = new Handler();
            } else {
                forceLoad();
            }
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    protected void readArguments(@NonNull Bundle args) {
        // implement it if you have arguments to retrieve
    }

}


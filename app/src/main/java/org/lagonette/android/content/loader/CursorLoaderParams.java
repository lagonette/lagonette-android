package org.lagonette.android.content.loader;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CursorLoaderParams {

    @NonNull
    private final Uri mUri;

    @NonNull
    private final String[] mProjection;

    @Nullable
    private String mSelection;

    @Nullable
    private String[] mSelectionArgs;

    @Nullable
    private String mSortOrder;

    public CursorLoaderParams(
            @NonNull Uri mUri,
            @NonNull String[] mProjection) {
        this.mUri = mUri;
        this.mProjection = mProjection;
    }

    @NonNull
    public Uri getUri() {
        return mUri;
    }

    @NonNull
    public String[] getProjection() {
        return mProjection;
    }

    @Nullable
    public String getSelection() {
        return mSelection;
    }

    @Nullable
    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    @Nullable
    public String getSortOrder() {
        return mSortOrder;
    }

    @NonNull
    public CursorLoaderParams setSelection(@Nullable String selection) {
        mSelection = selection;
        return CursorLoaderParams.this;
    }

    @NonNull
    public CursorLoaderParams setSelectionArgs(@Nullable String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
        return CursorLoaderParams.this;
    }

    @NonNull
    public CursorLoaderParams setSortOrder(@Nullable String sortOrder) {
        mSortOrder = sortOrder;
        return CursorLoaderParams.this;
    }
}

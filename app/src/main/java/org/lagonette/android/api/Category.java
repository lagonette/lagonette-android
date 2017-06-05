package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.LaGonetteContract;

import java.util.List;

public class Category implements ContentObject {

    @SerializedName("id")
    private long mId;

    @SerializedName("label")
    private String mLabel;

    @SerializedName("icon")
    private String mIcon;

    @Override
    public void prepareInsert(@NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues) {
        // Insert Category
        contentValues.clear();
        contentValues.put(LaGonetteContract.Category.ID, mId);
        contentValues.put(LaGonetteContract.Category.LABEL, mLabel);
        contentValues.put(LaGonetteContract.Category.ICON, mIcon);
        operations.add(
                ContentProviderOperation
                        .newInsert(LaGonetteContract.Category.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );

        // Insert Category metadata
        contentValues.clear();
        contentValues.put(LaGonetteContract.CategoryMetadata.CATEGORY_ID, mId);
        contentValues.put(LaGonetteContract.CategoryMetadata.IS_VISIBLE, true);
        contentValues.put(LaGonetteContract.CategoryMetadata.IS_COLLAPSED, false);
        operations.add(
                ContentProviderOperation
                        .newInsert(LaGonetteContract.CategoryMetadata.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );
    }
}

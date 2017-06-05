package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.LaGonetteContract;

import java.util.List;

public class CategoriesResponse extends ApiResponse {

    @SerializedName("categories")
    private List<Category> mCategories;

    @Override
    public void prepareInsert(@NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues) {
        operations.add(ContentProviderOperation
                .newDelete(LaGonetteContract.PartnerSideCategories.CONTENT_URI)
                .withYieldAllowed(true)
                .build()
        );
        for (Category category : mCategories) {
            category.prepareInsert(operations, contentValues);
        }
    }
}

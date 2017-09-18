package org.lagonette.app.api.response;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.util.PreferenceUtil;

import java.util.List;

public class CategoriesResponse extends ApiResponse {

    @SerializedName("categories")
    private List<Category> mCategories;

    public boolean prepareInsert(
            @NonNull Context context,
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String md5Sum = preferences.getString(
                PreferenceUtil.KEY_CATEGORY_MD5_SUM,
                PreferenceUtil.DEFAULT_VALUE_CATEGORY_MD5_SUM
        );
        addOfficeCategory(categories, categoryMetadataList);

        if (!mMd5Sum.equals(md5Sum)) {

            for (Category category : mCategories) {
                category.prepareInsert(categories, categoryMetadataList);
            }

            // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
            preferences.edit()
                    .putString(PreferenceUtil.KEY_CATEGORY_MD5_SUM, mMd5Sum)
                    .apply();

            return true;
        }

        return false;
    }

    private void addOfficeCategory(@NonNull List<org.lagonette.app.room.entity.Category> categories, @NonNull List<CategoryMetadata> categoryMetadataList) {
//        categories.add(new OfficeCategory());
//        categoryMetadataList.add(new OfficeCategoryMetadata());
    }
}

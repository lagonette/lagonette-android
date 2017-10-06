package org.lagonette.app.api.client;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CategoryClient extends ApiClient<CategoriesResponse> {

    private static final String TAG = "CategoryClient";

    @NonNull
    private final LaGonetteDatabase mDatabase;

    @NonNull
    private final LaGonetteService.Category mService;

    @NonNull
    private String mMd5Sum;

    public CategoryClient(
            @NonNull LaGonetteDatabase database,
            @NonNull LaGonetteService.Category service) {
        mDatabase = database;
        mService = service;
    }

    @NonNull
    @Override
    protected Call<CategoriesResponse> createCall() {
        return mService.getCategories();
    }

    @Override
    protected void onSuccessfulResponse(int code, @NonNull CategoriesResponse body) throws ApiClientException {
        List<Category> categories = new ArrayList<>();
        List<CategoryMetadata> categoryMetadataList = new ArrayList<>();

        mMd5Sum = body.md5Sum;
        body.prepareInsert(categories, categoryMetadataList);
        mDatabase.categoryDao().deleteCategories();
        mDatabase.categoryDao().insertCategories(categories);
        mDatabase.categoryDao().insertCategoriesMetadatas(categoryMetadataList); // TODO Make metadata insert only by SQL
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
        throw new ApiClientException("Response is not successful!");
    }

    @Nullable
    public String getLocalMd5Sum(@NonNull SharedPreferences preferences) {
        return preferences.getString(
                PreferenceUtil.KEY_CATEGORY_MD5_SUM,
                PreferenceUtil.DEFAULT_VALUE_CATEGORY_MD5_SUM
        );
    }

    public void saveRemoteMd5Sum(@NonNull SharedPreferences preferences) {
        preferences.edit()
                .putString(PreferenceUtil.KEY_CATEGORY_MD5_SUM, mMd5Sum)
                .apply();
    }
}

package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class ApiResponse implements ContentObject {

    @SerializedName("errors")
    protected String mErrors;

    @SerializedName("md5_sum")
    protected String mMd5Sum;

}

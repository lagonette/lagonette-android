package org.lagonette.app.api.response;

import com.google.gson.annotations.SerializedName;

public abstract class ApiResponse {

    @SerializedName("errors")
    protected String mErrors;

    @SerializedName("md5_sum")
    protected String mMd5Sum;

}

package org.lagonette.app.api.response;

import com.google.gson.annotations.SerializedName;

public abstract class ApiResponse {

    @SerializedName("errors")
    public String errors;

    @SerializedName("md5_sum")
    public String md5Sum;

}

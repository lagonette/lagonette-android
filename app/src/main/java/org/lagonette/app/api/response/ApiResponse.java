package org.lagonette.app.api.response;

import com.google.gson.annotations.SerializedName;

public abstract class ApiResponse extends Md5SumResponse {

    @SerializedName("errors")
    public String errors;

}

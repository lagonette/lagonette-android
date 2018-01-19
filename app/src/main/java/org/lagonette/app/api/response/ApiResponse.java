package org.lagonette.app.api.response;

import com.squareup.moshi.Json;

public abstract class ApiResponse extends Md5SumResponse {

    @Json(name = "errors")
    public String errors;

}

package org.lagonette.app.api.response;

import com.squareup.moshi.Json;

public class Md5SumResponse {

	@Json(name = "md5_sum")
	public String md5Sum;
}

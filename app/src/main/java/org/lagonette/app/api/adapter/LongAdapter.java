package org.lagonette.app.api.adapter;

import android.text.TextUtils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class LongAdapter {

	@FromJson
	public Long fromJson(@Nullable String string) {
		return TextUtils.isEmpty(string) ? null : Long.valueOf(string);
	}

	@ToJson
	public String toJson(Long value) {
		return String.valueOf(value);
	}
}
package org.lagonette.app.api.adapter;

import android.text.TextUtils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LongAdapter {

	//TODO If problem is experienced with proguard, see: https://github.com/square/moshi/issues/114

	@Retention(RetentionPolicy.RUNTIME)
	@interface Nullable {

	}

	@FromJson
	public Long fromJson(@Nullable String string) {
		return TextUtils.isEmpty(string) ? null : Long.valueOf(string);
	}

	@ToJson
	public String toJson(Long value) {
		return String.valueOf(value);
	}
}
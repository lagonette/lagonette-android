package org.lagonette.app.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class BooleanAdapter {

	private static final String TRUE = "1";

	private static final String FALSE = "0";

	@FromJson
	public boolean fromJson(@MoshiNullable String bool) {
		return TRUE.equals(bool);
	}

	@ToJson
	public String toJson(boolean bool) {
		return bool ? TRUE : FALSE;
	}
}
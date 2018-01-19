package org.lagonette.app.api.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BooleanAdapter {

    //TODO If problem is experienced with proguard, see: https://github.com/square/moshi/issues/114

    @Retention(RetentionPolicy.RUNTIME)
    @interface Nullable {
    }

    private static final String TRUE = "1";

    private static final String FALSE = "0";

    @FromJson
    public boolean fromJson(@Nullable String bool) {
        return TRUE.equals(bool);
    }

    @ToJson
    public String toJson(boolean bool) {
        return bool ? TRUE : FALSE;
    }
}
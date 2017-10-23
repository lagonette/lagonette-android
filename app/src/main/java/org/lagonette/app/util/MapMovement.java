package org.lagonette.app.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MapMovement {
//TODO Useless?
    public static final int MY_LOCATION = 0;
    public static final int FOOTPRINT = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MY_LOCATION,
            FOOTPRINT
    })
    public @interface Movement {
    }

    private MapMovement() {
    }
}

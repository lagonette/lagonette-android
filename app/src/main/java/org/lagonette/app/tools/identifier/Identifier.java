package org.lagonette.app.tools.identifier;

import android.util.Log;

public class Identifier {

    private static final String TAG = "Identifier";

    public static final int NO_TYPE = -1;

    private int mLastAddedType = NO_TYPE;

    private int mTypeCount;

    public Identifier(int typeCount) {
        mTypeCount = typeCount;
    }

    /**
     * Add a identifier type.
     *
     * @return the type. Keep it and use it to generate id.
     */
    public int addType() {
        mLastAddedType++;
        if (mLastAddedType >= mTypeCount) {
            mTypeCount = mLastAddedType + 1;
            Log.w(TAG, "Type count was changed, generated ids will not be consistent.");
        }
        return mLastAddedType;
    }

    public long gen(int type, long id) {
        if (type >= mTypeCount) {
            Log.w(TAG, "Type must be smaller than type count, generated ids could not be consistent.");
        } else if (type < 0) {
            Log.w(TAG, "Type must be equal or greater than 0, generated ids could not be consistent.");
        }

        return id * mTypeCount + type;
    }

}
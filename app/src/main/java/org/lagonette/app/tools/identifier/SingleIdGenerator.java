package org.lagonette.app.tools.identifier;

import android.support.annotation.NonNull;

public class SingleIdGenerator {

    @NonNull
    private final Identifier mIdentifier;

    private final int mIdentifierType;

    public SingleIdGenerator(@NonNull Identifier identifier) {
        mIdentifier = identifier;
        mIdentifierType = mIdentifier.addType();
    }

    public long genId(long id) {
       return mIdentifier.gen(mIdentifierType, id);
    }
}

package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.List;

public interface ContentObject {

    void prepareInsert(@NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues);

}

package org.lagonette.app.tools.arch;

import android.database.Cursor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;

public class CursorLiveData
        extends DbLiveData<Cursor> {

    @Nullable
    private Cursor mCursor;

    public CursorLiveData(
            @NonNull String[] tables,
            @NonNull Executor executor,
            @NonNull DataLoader<Cursor> dataLoader) {
        super(
                tables,
                executor,
                dataLoader
        );
    }

    @Override
    @MainThread
    public void setValue(Cursor newCursor) {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = newCursor;
        super.setValue(newCursor);
    }
}

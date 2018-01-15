package org.lagonette.app.tools.arch;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.room.statement.Statement;

public class StatementGuard extends NullSafeGuard<Long> {

    @NonNull
    @Override
    protected Long ensure(@Nullable Long value) {
        if (value == null || value < Statement.NO_ID) {
            return Statement.NO_ID;
        }
        else {
            return value;
        }
    }

}

package org.lagonette.app.room.embedded;

import android.arch.persistence.room.ColumnInfo;

public class CategoryKey {

    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "type")
    public long type;

    public long getUniqueId() {
        return id; //TODO correctly
    }

    public CategoryKey clone() {
        CategoryKey key = new CategoryKey();
        key.id = id;
        key.type = type;
        return key;
    }
}

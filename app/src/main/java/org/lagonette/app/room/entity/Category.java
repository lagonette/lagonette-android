package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(
        tableName = "category",
        primaryKeys = {
                "id"
        }
)
public class Category {

    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "icon")
    public String icon;

    @ColumnInfo(name = "display_order")
    public int displayOrder;

    @ColumnInfo(name = "hidden")
    public boolean hidden = false;

}

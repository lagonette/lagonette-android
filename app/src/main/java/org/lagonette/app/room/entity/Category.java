package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        tableName = "category"
)
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "icon")
    public String icon;

    @ColumnInfo(name = "display_order")
    public int displayOrder;

}

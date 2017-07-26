package org.lagonette.android.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(
        primaryKeys = {
                "partner_id",
                "category_id"
        },
        foreignKeys = {
                @ForeignKey(
                        entity = Partner.class,
                        parentColumns = "partner_id",
                        childColumns = "partner_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "category_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class PartnerSideCategory {

    @ColumnInfo(name = "partner_id")
    public long partnerId;

    @ColumnInfo(name = "category_id")
    public long categoryId;
}

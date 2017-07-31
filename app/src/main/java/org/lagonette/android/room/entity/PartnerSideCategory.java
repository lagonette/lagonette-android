package org.lagonette.android.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(
        tableName = "partner_side_category",
        primaryKeys = {
                "partner_id",
                "category_id"
        }//,
//        foreignKeys = {
//                @ForeignKey(
//                        entity = Partner.class,
//                        parentColumns = "id",
//                        childColumns = "partner_id",
//                        onDelete = ForeignKey.CASCADE
//                ),
//                @ForeignKey(
//                        entity = Category.class,
//                        parentColumns = "id",
//                        childColumns = "category_id",
//                        onDelete = ForeignKey.CASCADE
//                )
//        }
)
public class PartnerSideCategory {

    @ColumnInfo(name = "partner_id")
    public long partnerId;

    @ColumnInfo(name = "category_id")
    public long categoryId;
}

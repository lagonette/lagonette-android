package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

import org.lagonette.app.room.embedded.CategoryKey;

@Entity(
        tableName = "partner_side_category",
        primaryKeys = {
                "partner_id",
                "category_id",
                "category_type"
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

    @Embedded(prefix = "category_")
    public CategoryKey categoryKey;

}

package org.lagonette.app.room.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        tableName = "partner_metadata",
        foreignKeys = @ForeignKey(
                entity = Partner.class,
                parentColumns = "id",
                childColumns = "partner_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class PartnerMetadata {

    @PrimaryKey
    @ColumnInfo(name = "partner_id")
    public long partnerId;

    @ColumnInfo(name = "is_visible")
    public boolean isVisible;
}

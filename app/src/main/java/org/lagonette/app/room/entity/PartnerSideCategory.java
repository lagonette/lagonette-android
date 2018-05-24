package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(
		tableName = "partner_side_category",
		primaryKeys = {
				"partner_id",
				"category_id"
		}
)
public class PartnerSideCategory {

	@ColumnInfo(name = "partner_id")
	public long partnerId;

	@ColumnInfo(name = "category_id")
	public long categoryId;

}

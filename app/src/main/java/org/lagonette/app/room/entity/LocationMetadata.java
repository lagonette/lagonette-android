package org.lagonette.app.room.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(
		tableName = "location_metadata",
		foreignKeys = @ForeignKey(
				entity = Location.class,
				parentColumns = "id",
				childColumns = "location_id",
				onDelete = ForeignKey.CASCADE
		)
)
public class LocationMetadata {

	@PrimaryKey
	@ColumnInfo(name = "location_id")
	public long locationId;

	@ColumnInfo(name = "is_visible")
	public boolean isVisible;
}

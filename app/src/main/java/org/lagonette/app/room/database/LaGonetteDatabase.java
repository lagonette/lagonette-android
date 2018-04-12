package org.lagonette.app.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.lagonette.app.room.converter.DataTypeConverters;
import org.lagonette.app.room.dao.CategoryDao;
import org.lagonette.app.room.dao.PartnerDao;
import org.lagonette.app.room.dao.UiDao;
import org.lagonette.app.room.dao.WriterDao;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.util.DatabaseUtils;

@Database(
		entities = {
				Partner.class,
				LocationMetadata.class,
				Location.class,
				Category.class,
				CategoryMetadata.class,
				PartnerSideCategory.class,
		},
		version = DatabaseUtils.DATABASE_NUMBER
)
@TypeConverters({DataTypeConverters.class})
public abstract class LaGonetteDatabase
		extends RoomDatabase {

	public abstract UiDao uiDao();

	public abstract WriterDao writerDao();

	public abstract PartnerDao partnerDao();

	public abstract CategoryDao categoryDao();


}
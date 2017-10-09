package org.lagonette.app.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.lagonette.app.room.dao.CategoryDao;
import org.lagonette.app.room.dao.MainDao;
import org.lagonette.app.room.dao.PartnerDao;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerMetadata;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.util.DatabaseUtil;

@Database(
        entities = {
                Partner.class,
                PartnerMetadata.class,
                Location.class,
                Category.class,
                CategoryMetadata.class,
                PartnerSideCategory.class,
        },
        version = DatabaseUtil.DATABASE_NUMBER
)
public abstract class LaGonetteDatabase extends RoomDatabase {

    public abstract MainDao mainDao();

    public abstract PartnerDao partnerDao();

    public abstract CategoryDao categoryDao();


}
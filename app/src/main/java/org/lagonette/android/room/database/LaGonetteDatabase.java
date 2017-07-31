package org.lagonette.android.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.lagonette.android.room.dao.CategoryDao;
import org.lagonette.android.room.dao.MainDao;
import org.lagonette.android.room.dao.PartnerDao;
import org.lagonette.android.room.entity.Category;
import org.lagonette.android.room.entity.CategoryMetadata;
import org.lagonette.android.room.entity.Partner;
import org.lagonette.android.room.entity.PartnerMetadata;
import org.lagonette.android.room.entity.PartnerSideCategory;
import org.lagonette.android.util.DatabaseUtil;

@Database(
        entities = {
                Partner.class,
                PartnerMetadata.class,
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
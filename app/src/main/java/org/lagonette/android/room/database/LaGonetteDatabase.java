package org.lagonette.android.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.lagonette.android.room.dao.LaGonetteDao;
import org.lagonette.android.room.entity.Category;
import org.lagonette.android.room.entity.CategoryMetadata;
import org.lagonette.android.room.entity.Partner;
import org.lagonette.android.room.entity.PartnerMetadata;
import org.lagonette.android.room.entity.PartnerSideCategory;

@Database(
        entities = {
                Partner.class,
                PartnerMetadata.class,
                Category.class,
                CategoryMetadata.class,
                PartnerSideCategory.class,
        },
        version = 1
)
public abstract class LaGonetteDatabase extends RoomDatabase {

    public abstract LaGonetteDao partnerDao();

}
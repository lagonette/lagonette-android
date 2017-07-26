package org.lagonette.android.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.lagonette.android.room.dao.PartnerDao;
import org.lagonette.android.room.entity.Partner;

@Database(entities = {Partner.class}, version = 1)
public abstract class LaGonetteDatabase extends RoomDatabase {

    public abstract PartnerDao partnerDao();

}
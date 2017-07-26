package org.lagonette.android.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.lagonette.android.room.entity.Partner;

@Dao
public interface PartnerDao {

    @Query("SELECT * FROM partner WHERE id = :id")
    Partner getPartner(int id);
}

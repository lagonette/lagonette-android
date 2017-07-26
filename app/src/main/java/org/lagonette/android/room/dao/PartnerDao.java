package org.lagonette.android.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.lagonette.android.room.entity.Partner;

import java.util.List;

@Dao
public interface PartnerDao {

    @Query("SELECT * FROM partner")
    List<Partner> getAll();

    @Query("SELECT * FROM partner WHERE uid IN (:userIds)")
    List<Partner> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM partner WHERE name LIKE :first LIMIT 1")
    Partner findByName(String first, String last);

    @Insert
    void insertAll(Partner... users);

    @Delete
    void delete(Partner user);
}
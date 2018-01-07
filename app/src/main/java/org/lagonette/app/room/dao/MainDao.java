package org.lagonette.app.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.room.statement.MapPartnerStatement;
import org.lagonette.app.room.statement.PartnerDetailStatement;

import java.util.List;

@Dao
public interface MainDao {

    @Query(PartnerDetailStatement.SQL)
    LiveData<LocationDetail> getLocationsDetail(long id); //TODO Use LiveData to update fragment ?

    @Query(MapPartnerStatement.SQL)
    LiveData<List<LocationItem>> getMapLocations(String search);

    @Query(FilterStatement.SQL)
    Cursor getFilters(String search);
}
package org.lagonette.app.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.room.statement.MapPartnerStatement;
import org.lagonette.app.room.statement.PartnerDetailStatement;

import java.util.List;

@Dao
public interface UiDao {

    @Query(PartnerDetailStatement.SQL)
    LiveData<LocationDetail> getLocationsDetail(long id); //TODO Use LiveData to update fragment ?

    @Query(MapPartnerStatement.SQL)
    LiveData<List<LocationItem>> getMapLocations(String search);

    @Query(FilterStatement.SQL)
    DataSource.Factory<Integer, Filter> getFilters(String search);

    @Query("SELECT location.id AS location_id, partner.logo AS icon " +
            "FROM partner " +
            "JOIN location ON partner.id = location.partner_id " +
            "WHERE partner.is_gonette_headquarter = 1 " +
            "LIMIT 1")
    LiveData<HeadquarterShortcut> getHeadquarterShortcut();
}
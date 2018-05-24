package org.lagonette.app.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.entity.statement.Shortcut;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.room.statement.MapLocationStatement;
import org.lagonette.app.room.statement.LocationDetailStatement;
import org.lagonette.app.room.statement.ShortcutStatement;

import java.util.List;

@Dao
public interface UiDao {

	@Query(LocationDetailStatement.SQL)
	LiveData<LocationDetail> getLocationsDetail(long id);

	@Query(MapLocationStatement.LOCATIONS_SQL)
	LiveData<List<LocationItem>> getMapLocations(String search);

	@Query(MapLocationStatement.LOCATION_SQL)
	LiveData<LocationItem> getMapLocation(long id);

	@Query(FilterStatement.SQL)
	DataSource.Factory<Integer, Filter> getFilters(String search);

	@Query(ShortcutStatement.SQL)
	LiveData<Shortcut> getShortcut();
}
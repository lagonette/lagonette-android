package org.lagonette.app.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import org.lagonette.app.room.entity.statement.PartnerDetail;
import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.room.statement.MapPartnerStatement;
import org.lagonette.app.room.statement.PartnerDetailStatement;

import java.util.List;

@Dao
public interface MainDao {

    @Query(PartnerDetailStatement.sql) // TODO Use LiveData to update fragment ?
    LiveData<PartnerDetail> getPartnerDetail(long id);

    @Query(MapPartnerStatement.sql)
    LiveData<List<PartnerItem>> getMapPartner(String search);

    @Query(FilterStatement.sql)
    Cursor getFilters(String search);
}
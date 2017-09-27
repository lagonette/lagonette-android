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

    @Query(PartnerDetailStatement.SQL)
    LiveData<PartnerDetail> getPartnerDetail(long id); // TODO Use LiveData to update fragment ?

    @Query(MapPartnerStatement.SQL)
    LiveData<List<PartnerItem>> getMapPartner(String search);

    @Query(FilterStatement.SQL)
    Cursor getFilters(String search);
}
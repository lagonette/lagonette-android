package org.lagonette.android.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.lagonette.android.room.entity.Partner;
import org.lagonette.android.room.entity.PartnerMetadata;
import org.lagonette.android.room.entity.PartnerSideCategory;

import java.util.List;

@Dao
public interface PartnerDao {

    @Insert
    long[] insertPartners(List<Partner> partners);

    // TODO Use foreign key cascade
    @Query("DELETE FROM partner")
    void deletePartners();

    // TODO Use foreign key cascade
    @Query("DELETE FROM partner_side_category")
    void deletePartnerSideCategories();

    @Insert
    long[] insertPartnersMetadatas(List<PartnerMetadata> partnersMetadatas);

    @Insert
    long[] insertPartnersSideCategories(List<PartnerSideCategory> partnersSideCategories);

    @Query("UPDATE partner_metadata SET is_visible = :isVisible WHERE partner_id = :id")
    int updatePartnerVisibility(long id, boolean isVisible);

}

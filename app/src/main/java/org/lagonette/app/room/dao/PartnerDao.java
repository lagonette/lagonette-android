package org.lagonette.app.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.PartnerSideCategory;

import java.util.List;

@Dao
public interface PartnerDao {

    @Insert
    long[] insertPartners(List<Partner> partners);

    @Insert
    long[] insertLocations(List<Location> locations);

    // TODO Use foreign key cascade
    @Query("DELETE FROM partner")
    void deletePartners();

    // TODO Use foreign key cascade and take care of metadata
    @Query("DELETE FROM location")
    void deleteLocations();

    // TODO Use foreign key cascade
    @Query("DELETE FROM partner_side_category")
    void deletePartnerSideCategories();

    @Insert
    long[] insertLocationsMetadatas(List<LocationMetadata> locationsMetadatas);

    @Insert
    long[] insertPartnersSideCategories(List<PartnerSideCategory> partnersSideCategories);

    @Query("UPDATE location_metadata SET is_visible = :isVisible WHERE location_id = :id")
    int updatePartnerVisibility(long id, boolean isVisible);

    @Query("UPDATE location_metadata SET is_visible = :isVisible")
    int updatePartnerVisibilities(boolean isVisible);

//    @Query("UPDATE partner_metadata SET is_visible = :isVisible WHERE partner_id IN (SELECT id FROM partner WHERE is_exchange_office <> 0)")
//    int updateExchangeOfficeVisibilities(boolean isVisible);

    @Query("DELETE FROM location WHERE (street IS NULL OR street = '') AND latitude = 0 AND longitude = 0")
    void cleanLocation();

    @Query("DELETE FROM partner WHERE id NOT IN (SELECT partner.id FROM partner JOIN location ON location.partner_id = partner.id)")
    void cleanPartner();
}

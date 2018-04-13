package org.lagonette.app.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerSideCategory;

import java.util.List;

@SuppressWarnings("NullableProblems")
@Dao
public interface PartnerDao {

	@Query("SELECT * FROM partner WHERE is_gonette_headquarter = 1")
	Partner getHeadquarter();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertPartner(@NonNull Partner partner);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long[] insertPartners(List<Partner> partners);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long[] insertLocations(List<Location> locations);

	@Query("DELETE FROM partner")
	void deletePartners();

	@Query("DELETE FROM location")
	void deleteLocations();

	@Query("DELETE FROM partner_side_category")
	void deletePartnerSideCategories();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long[] insertLocationsMetadatas(List<LocationMetadata> locationsMetadatas);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long[] insertPartnersSideCategories(List<PartnerSideCategory> partnersSideCategories);

	@Query("UPDATE location_metadata SET is_visible = :isVisible WHERE location_id = :id")
	int updateLocationVisibility(long id, boolean isVisible);

	@Query("UPDATE location_metadata SET is_visible = :isVisible")
	int updateLocationVisibilities(boolean isVisible);

	@Query("UPDATE location_metadata SET is_visible = :isVisible WHERE location_id IN (SELECT id FROM location WHERE is_exchange_office <> 0)")
	int updateExchangeOfficeVisibilities(boolean isVisible);

	@Query("DELETE FROM location WHERE (street IS NULL OR street = '') AND latitude = 0 AND longitude = 0")
	void cleanLocationWithoutPosition();

	@Query("DELETE FROM partner WHERE id NOT IN (SELECT partner.id FROM partner JOIN location ON location.partner_id = partner.id)")
	void cleanPartnerWithoutLocation();

	@Query("DELETE FROM location_metadata WHERE location_id NOT IN (SELECT id FROM location)")
	void cleanOrphanLocationMetadata();

	@Query("DELETE FROM partner_side_category WHERE category_id NOT IN (SELECT category.id FROM category) OR partner_id NOT IN (SELECT partner.id FROM partner) ")
	void cleanOrphanPartnerSideCategory();

	@Delete
	void deletePartner(@NonNull Partner headquarter);
}

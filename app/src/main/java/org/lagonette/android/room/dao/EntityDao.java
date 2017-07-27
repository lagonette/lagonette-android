package org.lagonette.android.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.lagonette.android.room.entity.Category;
import org.lagonette.android.room.entity.CategoryMetadata;
import org.lagonette.android.room.entity.Partner;
import org.lagonette.android.room.entity.PartnerMetadata;

import java.util.List;

@Dao
public interface EntityDao {

    @Query("SELECT * FROM partner WHERE id = :id")
    Partner getPartner(int id);

    @Insert
    long[] insertPartners(List<Partner> partners);

    @Insert
    long[] insertPartnersMetadatas(List<PartnerMetadata> partnersMetadatas);

    @Insert
    long[] insertCategories(List<Category> categories);

    @Insert
    long[] insertCategoriesMetadatas(List<CategoryMetadata> categoriesMetadatas);

    @Insert
    long[] insertPartnersSideCategories(List<CategoryMetadata> partnersSideCategories);

}

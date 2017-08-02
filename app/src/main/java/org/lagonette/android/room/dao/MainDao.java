package org.lagonette.android.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.lagonette.android.room.statement.MapPartnerStatement;
import org.lagonette.android.room.statement.PartnerDetailStatement;

@Dao
public interface MainDao {

    // TODO Return Partner ?
    @Query(PartnerDetailStatement.sql)
    Cursor getPartnerDetail(long id);

    // TODO Return reader (TypeAdapter) ?
    @Query(MapPartnerStatement.sql)
    Cursor getMapPartner();

    @Query("SELECT 0 AS row_type, " +
                    "category.id, " +
                    "category.label, " +
                    "category.icon, " +
                    "category_metadata.is_visible, " +
                    "category_metadata.is_collapsed, " +
                    "SUM (main_partner_metadata.is_visible) AS main_partner_visibility_sum, " +
                    "SUM (side_partner_metadata.is_visible) AS side_partner_visibility_sum, " +
                    "NULL AS id, " +
                    "NULL AS name, " +
                    "NULL AS street, " +
                    "NULL AS zip_code, " +
                    "NULL AS city, " +
                    "NULL AS is_exchange_office, " +
                    "NULL AS is_visible " +
            "FROM category " +
            "JOIN category_metadata " +
                "ON category.id = category_metadata.category_id " +
            "LEFT JOIN partner AS main_partner " +
                "ON category.id = main_partner.main_category " +
            "LEFT JOIN partner_metadata AS main_partner_metadata " +
                "ON main_partner.id = main_partner_metadata.partner_id " +
            "LEFT JOIN partner_side_category " +
                "ON category.id = partner_side_category.category_id " +
            "LEFT JOIN partner AS side_partner " +
                "ON partner_side_category.partner_id = side_partner.id " +
            "LEFT JOIN partner_metadata AS side_partner_metadata " +
                "ON side_partner.id = side_partner_metadata.partner_id " +
            "WHERE main_partner.name LIKE :search " +
                "OR side_partner.name LIKE :search " +
            "GROUP BY category.id " +

            "UNION " +

            "SELECT " +
                "3 AS row_type, " +
                "category.id, " +
                "NULL AS label, " +
                "NULL AS icon, " +
                "NULL AS is_visible, " +
                "NULL AS is_collapsed, " +
                "NULL AS main_partner_visibility_sum, " +
                "NULL AS side_partner_visibility_sum, " +
                "NULL AS id, " +
                "NULL AS name, " +
                "NULL AS street, " +
                "NULL AS zip_code, " +
                "NULL AS city, " +
                "NULL AS is_exchange_office, " +
                "NULL AS is_visible " +
            "FROM category " +
            "LEFT JOIN partner AS main_partner " +
                "ON category.id = main_partner.main_category " +
            "LEFT JOIN partner_side_category " +
                "ON category.id = partner_side_category.category_id " +
            "LEFT JOIN partner AS side_partner " +
                "ON partner_side_category.partner_id = side_partner.id " +
            "WHERE main_partner.main_category IS NOT NULL " +
                "AND main_partner.name LIKE :search " +
                "OR category.id IS NOT NULL " +
                "AND side_partner.name LIKE :search " +
            "GROUP BY category.id " +

            "UNION " +

            "SELECT 1 AS row_type, " +
                "category.id, " +
                "NULL AS label, " +
                "NULL AS icon, " +
                "category_metadata.is_visible, " +
                "NULL AS is_collapsed, " +
                "NULL AS main_partner_visibility_sum, " +
                "NULL AS side_partner_visibility_sum, " +
                "partner.id, " +
                "partner.name, " +
                "partner.street, " +
                "partner.zip_code, " +
                "partner.city, " +
                "partner.is_exchange_office, " +
                "partner_metadata.is_visible " +
            "FROM category " +
            "JOIN category_metadata " +
                "ON category.id = category_metadata.category_id " +
            "JOIN partner " +
                "ON category.id = partner.main_category " +
            "JOIN partner_metadata " +
                "ON partner.id = partner_metadata.partner_id " +
            "WHERE partner.name LIKE :search AND category_metadata.is_collapsed = 0 " +

            "UNION " +

            "SELECT 2 AS row_type, " +
                "category.id, " +
                "NULL AS label, " +
                "NULL AS icon, " +
                "category_metadata.is_visible, " +
                "NULL AS is_collapsed, " +
                "NULL AS main_partner_visibility_sum, " +
                "NULL AS side_partner_visibility_sum, " +
                "partner.id, " +
                "partner.name, " +
                "partner.street, " +
                "partner.zip_code, " +
                "partner.city, " +
                "partner.is_exchange_office, " +
                "partner_metadata.is_visible " +
            "FROM category " +
            "JOIN category_metadata " +
                "ON category.id = category_metadata.category_id " +
            "JOIN partner_side_category " +
                "ON category.id = partner_side_category.category_id " +
            "JOIN partner " +
                "ON partner.id = partner_side_category.partner_id " +
            "JOIN partner_metadata " +
                "ON partner.id = partner_metadata.partner_id " +
            "WHERE partner.name LIKE :search " +
                "AND category_metadata.is_collapsed = 0 " +

            "ORDER BY category.id ASC, row_type ASC"
    )
    Cursor getFilters(@NonNull String search);
}
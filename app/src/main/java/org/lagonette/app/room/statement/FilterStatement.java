package org.lagonette.app.room.statement;

import android.support.annotation.IntDef;

import org.lagonette.app.room.statement.base.GonetteStatement;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public abstract class FilterStatement
		implements GonetteStatement {

	public static final int VALUE_ROW_CATEGORY = 1;

	public static final int VALUE_ROW_MAIN_PARTNER = 2;

	public static final int VALUE_ROW_FOOTER = 3;

	public static final int ROW_TYPE_COUNT = 4;

	private static final String SQL_CATEGORIES =
			"SELECT " +
					VALUE_ROW_CATEGORY + " AS row_type, " +
					"category.id AS category_id, " +
					"category.label AS category_label, " +
					"category.icon AS category_icon, " +
					"category.display_order AS category_display_order, " +
					"category_metadata.is_visible AS category_is_visible, " +
					"category_metadata.is_collapsed AS category_is_collapsed, " +
					"TOTAL(main_location_metadata.is_visible) AS category_is_partners_visible, " +
					"NULL AS location_id, " +
					"NULL AS location_street, " +
					"NULL AS location_zip_code, " +
					"NULL AS location_city, " +
					"NULL AS location_is_exchange_office, " +
					"NULL AS location_is_visible, " +
					"NULL AS partner_name" +
					FROM_CATEGORY_AND_METADATA +
					LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY +
					LEFT_JOIN_MAIN_LOCATION_AND_METADATA_ON_MAIN_PARTNER +
					" WHERE category.hidden = 0 " +
					" AND main_partner.name LIKE :search " +
					" GROUP BY category.id ";

	private static final String SQL_FOOTERS =
			"SELECT " +
					VALUE_ROW_FOOTER + " AS row_type, " +
					"category.id AS category_id, " +
					"NULL AS category_label, " +
					"NULL AS category_icon, " +
					"category.display_order AS category_display_order, " +
					"NULL AS category_is_visible, " +
					"NULL AS category_is_collapsed, " +
					"NULL AS category_is_partners_visible, " +
					"NULL AS location_id, " +
					"NULL AS location_street, " +
					"NULL AS location_zip_code, " +
					"NULL AS location_city, " +
					"NULL AS location_is_exchange_office, " +
					"NULL AS location_is_visible, " +
					"NULL AS partner_name" +
					FROM_CATEGORY +
					LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY +
					LEFT_JOIN_MAIN_LOCATION_ON_MAIN_PARTNER +
					" WHERE main_partner.main_category_id IS NOT NULL " +
					" AND main_partner.name LIKE :search " +
					" GROUP BY category.id ";

	private static final String SQL_MAIN_PARTNERS =
			"SELECT " +
					VALUE_ROW_MAIN_PARTNER + " AS row_type, " +
					"category.id AS category_id, " +
					"NULL AS category_label, " +
					"NULL AS category_icon, " +
					"category.display_order AS category_display_order, " +
					"category_metadata.is_visible AS category_is_visible, " +
					"NULL AS category_is_collapsed, " +
					"NULL AS category_is_partners_visible, " +
					"main_location.id AS location_id, " +
					"main_location.street AS location_street, " +
					"main_location.zip_code AS location_zip_code, " +
					"main_location.city AS location_city, " +
					"main_location.is_exchange_office AS location_is_exchange_office, " +
					"main_location_metadata.is_visible AS location_is_visible, " +
					"main_partner.name AS partner_name" +
					FROM_CATEGORY_AND_METADATA +
					JOIN_MAIN_PARTNER_ON_CATEGORY +
					LEFT_JOIN_MAIN_LOCATION_AND_METADATA_ON_MAIN_PARTNER +
					" WHERE main_partner.name LIKE :search AND category_metadata.is_collapsed = 0 ";

	public static final String SQL =
			SQL_CATEGORIES +
					" UNION " +
					SQL_FOOTERS +
					" UNION " +
					SQL_MAIN_PARTNERS +
					" ORDER BY category.display_order ASC, category.id ASC, row_type ASC";

	@Retention(SOURCE)
	@IntDef({
			VALUE_ROW_CATEGORY,
			VALUE_ROW_MAIN_PARTNER,
			VALUE_ROW_FOOTER
	})
	public @interface RowType {

	}

}
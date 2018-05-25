package org.lagonette.app.room.statement;

import org.lagonette.app.room.sql.Sql;
import org.lagonette.app.room.statement.base.GonetteStatement;

public abstract class MapLocationStatement
		implements GonetteStatement, Sql {

	public static final String LOCATIONS_SQL =
			"SELECT location.id, " +
					"partner.main_category_id, " +
					"partner.is_gonette_headquarter, " +
					"location.is_exchange_office, " +
					"location.latitude || ',' || location.longitude AS position, " +
					"main_category.icon " +
					FROM_PARTNER +
					JOIN_LOCATION_AND_METADATA_ON_PARTNER +
					LEFT_JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER +
					"WHERE partner.name LIKE :search " +
					"AND location.display_location <> 0 " +
					"AND location_metadata.is_visible <> 0 " +
					"GROUP BY location.id " +
					"HAVING main_category_metadata.is_visible " +
					"OR partner.main_category_id = " + Statement.NO_ID;

	public static final String LOCATION_SQL =
			"SELECT location.id, " +
					"partner.main_category_id, " +
					"partner.is_gonette_headquarter, " +
					"location.is_exchange_office, " +
					"location.latitude || ',' || location.longitude AS position, " +
					"main_category.icon " +
					FROM_PARTNER +
					JOIN_LOCATION_AND_METADATA_ON_PARTNER +
					LEFT_JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER +
					"WHERE location.id = :id " +
					"AND location.display_location <> 0 " +
					"GROUP BY location.id ";

}
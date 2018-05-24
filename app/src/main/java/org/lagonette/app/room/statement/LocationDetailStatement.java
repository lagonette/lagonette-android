package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;

import static org.lagonette.app.room.statement.Statement.NO_ID;

public interface LocationDetailStatement
		extends GonetteStatement {

	String SQL =
			"SELECT " +
					"partner.id, " +
					"partner.client_code, " +
					"partner.logo, " +
					"partner.name, " +
					"partner.short_description, " +
					"partner.description, " +
					"partner.phone, " +
					"partner.website, " +
					"partner.email, " +
					"partner.main_category_id, " +
					"partner.is_gonette_headquarter, " +
					"location.latitude, " +
					"location.longitude, " +
					"location.opening_hours, " +
					"location.is_exchange_office, " +
					"location.street, " +
					"location.zip_code, " +
					"location.city, " +
					"(CASE WHEN main_category.id IS NULL THEN " + NO_ID + " ELSE main_category.id END) AS main_category_id, " +
					"main_category.label AS main_category_label, " +
					"main_category.icon AS main_category_icon" +
					FROM_PARTNER +
					JOIN_LOCATION_ON_PARTNER +
					LEFT_JOIN_MAIN_CATEGORY_ON_PARTNER +
					" WHERE location.id = :id " +
					" LIMIT 1";
}

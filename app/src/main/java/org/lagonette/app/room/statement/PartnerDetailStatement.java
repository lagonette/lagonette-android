package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;

public interface PartnerDetailStatement
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
					"main_category.id as main_category_id, " +
					"main_category.label as main_category_label, " +
					"main_category.icon as main_category_icon" +
					FROM_PARTNER +
					JOIN_LOCATION_ON_PARTNER +
					LEFT_JOIN_MAIN_CATEGORY_ON_PARTNER +
					" WHERE location.id = :id " +
					" LIMIT 1";
}

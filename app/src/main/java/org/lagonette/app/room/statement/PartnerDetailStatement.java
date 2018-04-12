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
					"location.latitude, " +
					"location.longitude, " +
					"location.opening_hours, " +
					"location.is_exchange_office, " +
					"location.street, " +
					"location.zip_code, " +
					"location.city, " +
					"main_category.label, " +
					"main_category.icon " +
					FROM_PARTNER +
					JOIN_MAIN_CATEGORY_ON_PARTNER +
					JOIN_LOCATION_ON_PARTNER +
					" WHERE location.id = :id " +
					" LIMIT 1";
}

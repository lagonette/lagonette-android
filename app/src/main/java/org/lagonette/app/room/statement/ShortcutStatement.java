package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;

public abstract class ShortcutStatement
		implements GonetteStatement {

	public static final String SQL =
			"SELECT * " +
					" FROM (" +
					" SELECT location.id AS headquarter_location_id, partner.logo AS headquarter_icon " +
					FROM_PARTNER +
					JOIN_LOCATION_ON_PARTNER +
					" WHERE partner.is_gonette_headquarter = 1 " +
					" LIMIT 1" +
					" ), (" +
					" SELECT " +
					" SUM(location_metadata.is_visible) as visible_partner_count, " +
					" COUNT(1) AS partner_count, " +
					" SUM(CASE WHEN location.is_exchange_office > 0 THEN location_metadata.is_visible ELSE 0 END) AS visible_exchange_office_count, " +
					" SUM(location.is_exchange_office) AS exchange_office_count " +
					FROM_LOCATION +
					JOIN_METADATA_ON_LOCATION +
					" ) " +
					" LIMIT 1";

}
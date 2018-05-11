package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;

public abstract class ShortcutStatement
		implements GonetteStatement {

	private static final String CONDITION_SQL =
			" SELECT 1 as there_is_some_partners" +
					FROM_PARTNER +
					" LIMIT 1";

	private static final String MAIN_SHORTCUT_SQL =
			" SELECT " +
					" SUM(location_metadata.is_visible) as visible_partner_count, " +
					" COUNT(1) AS partner_count, " +
					" SUM(CASE WHEN location.is_exchange_office > 0 THEN location_metadata.is_visible ELSE 0 END) AS visible_exchange_office_count, " +
					" SUM(location.is_exchange_office) AS exchange_office_count " +
					FROM_LOCATION +
					JOIN_METADATA_ON_LOCATION +
					" LIMIT 1";

	private static final String CATEGORY_SHORTCUT_SQL =
			" SELECT " +
					" TOTAL(category_metadata.is_collapsed) = COUNT(1) as is_all_category_collapsed, " +
					" TOTAL(category_metadata.is_visible) = COUNT(1) as is_all_category_visible " +
					FROM_CATEGORY_METADATA +
					" LIMIT 1";

	private static final String HEADQUARTER_SQL =
			" SELECT location.id AS headquarter_location_id, partner.logo AS headquarter_icon " +
					FROM_PARTNER +
					JOIN_LOCATION_ON_PARTNER +
					" WHERE partner.is_gonette_headquarter = 1 " +
					" LIMIT 1";

	public static final String SQL =
			"SELECT * " +
					" FROM (" +
					CONDITION_SQL +
					" )" +
					" LEFT JOIN (" +
					MAIN_SHORTCUT_SQL +
					" )" +
					" LEFT JOIN (" +
					CATEGORY_SHORTCUT_SQL +
					" )" +
					"LEFT JOIN (" +
					HEADQUARTER_SQL +
					" ) " +
					" LIMIT 1";

}
package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;

public interface PartnerDetailStatement
        extends GonetteStatement {

    String SQL =
            "SELECT " +
                    "partner.id, " +
                    "partner.name, " +
                    "partner.description, " +
                    "partner.latitude, " +
                    "partner.longitude, " +
                    "partner.client_code, " +
                    "partner.logo, " +
                    "partner.phone, " +
                    "partner.website, " +
                    "partner.email, " +
                    "partner.opening_hours, " +
                    "partner.is_exchange_office, " +
                    "partner.short_description, " +
                    "partner.main_category_id, " +
                    "partner.street, " +
                    "partner.zip_code, " +
                    "partner.city, " +
                    "category.label, " +
                    "category.icon " +
            FROM_PARTNER +
            JOIN_MAIN_CATEGORY_ON_PARTNER +
            "WHERE partner.id = :id " +
            "LIMIT 1";
}

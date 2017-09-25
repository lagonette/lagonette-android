package org.lagonette.app.room.statement;

public abstract class PartnerDetailStatement
        extends Statement {

    public static final String sql =
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
            "FROM partner " +
            "JOIN category " +
                    "ON partner.main_category_id = category.id " +
            "WHERE partner.id = :id " +
            "LIMIT 1";
}

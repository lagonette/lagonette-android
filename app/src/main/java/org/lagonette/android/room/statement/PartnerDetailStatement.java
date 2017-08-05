package org.lagonette.android.room.statement;

public class PartnerDetailStatement {

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

    public static int ID;

    public static int NAME;

    public static int DESCRIPTION;

    public static int LATITUDE;

    public static int LONGITUDE;

    public static int CLIENT_CODE;

    public static int LOGO;

    public static int PHONE;

    public static int WEBSITE;

    public static int EMAIL;

    public static int OPENING_HOURS;

    public static int IS_EXCHANGE_OFFICE;

    public static int SHORT_DESCRIPTION;

    public static int MAIN_CATEGORY_ID;

    public static int STREET;

    public static int ZIP_CODE;

    public static int CITY;

    public static int CATEGORY_LABEL;

    public static int CATEGORY_ICON;

    static {
        int i = 0;
        ID = i++;
        NAME = i++;
        DESCRIPTION = i++;
        LATITUDE = i++;
        LONGITUDE = i++;
        CLIENT_CODE = i++;
        LOGO = i++;
        PHONE = i++;
        WEBSITE = i++;
        EMAIL = i++;
        OPENING_HOURS = i++;
        IS_EXCHANGE_OFFICE = i++;
        SHORT_DESCRIPTION = i++;
        MAIN_CATEGORY_ID = i++;
        STREET = i++;
        ZIP_CODE = i++;
        CITY = i++;
        CATEGORY_LABEL = i++;
        CATEGORY_ICON = i++;
    }

    public interface Contract {

        long getId();

        String getName();

        String getDescription();

        double getLatitude();

        double getLongitude();

        String getLogo();

        String getPhone();

        String getWebsite();

        String getEmail();

        String getOpeningHours();

        boolean isExchangeOffice();

        String getShortDescription();

        long getMainCategoryId();

        String getAddress();

        String getCategoryLabel();

        String getCategoryIcon();
    }
}

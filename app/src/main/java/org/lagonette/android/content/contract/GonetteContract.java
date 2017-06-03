package org.lagonette.android.content.contract;

import android.net.Uri;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.database.Statements;
import org.lagonette.android.database.Tables;
import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.CategoryMetadataColumns;
import org.lagonette.android.database.columns.FilterColumns;
import org.lagonette.android.database.columns.MapColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.database.columns.PartnerSideCategoriesColumns;

public class GonetteContract {

    public static final String CONTENT_TYPE_APP_BASE = BuildConfig.APPLICATION_ID;

    public static final String CONTENT_TYPE_DIR_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_TYPE_ITEM_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String AUTHORITY = BuildConfig.CONTENT_AUTHORITY;

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final long NO_ID = -1;

    public interface Filter extends FilterColumns {

        interface Category extends CategoryColumns {
        }

        interface CategoryMetadata extends CategoryMetadataColumns {
        }

        interface Partner extends PartnerColumns {
        }

        interface PartnerMetadata extends PartnerMetadataColumns {
        }

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Statements.FILTERS)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Statements.FILTERS;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Statements.FILTERS;
    }

    public interface Map extends MapColumns {

        interface Category extends CategoryColumns {
        }

        interface CategoryMetadata extends CategoryMetadataColumns {
        }

        interface Partner extends PartnerColumns {
        }

        interface PartnerMetadata extends PartnerMetadataColumns {
        }

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Statements.MAP)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Statements.MAP;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Statements.MAP;
    }

    public interface Partner
            extends PartnerColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Tables.PARTNER)
                .build();

        Uri EXTENDED_CONTENT_URI = CONTENT_URI.buildUpon() // TODO Not in use ?
                .appendPath(Tables.PARTNER_METADATA)
                .appendPath(Tables.CATEGORY)
                .appendPath(Tables.CATEGORY_METADATA)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Tables.PARTNER;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Tables.PARTNER;
    }

    public interface Category
            extends CategoryColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Tables.CATEGORY)
                .build();

        Uri EXTENDED_CONTENT_URI = CONTENT_URI.buildUpon()
                .appendPath(Tables.CATEGORY_METADATA)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Tables.CATEGORY;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Tables.CATEGORY;
    }

    public interface PartnerSideCategories
            extends PartnerSideCategoriesColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Tables.PARTNER_SIDE_CATEGORIES)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Tables.PARTNER_SIDE_CATEGORIES;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Tables.PARTNER_SIDE_CATEGORIES;
    }

    public interface PartnerMetadata
            extends PartnerMetadataColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Tables.PARTNER_METADATA)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Tables.PARTNER_METADATA;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Tables.PARTNER_METADATA;
    }

    public interface CategoryMetadata
            extends CategoryMetadataColumns {

        Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(Tables.CATEGORY_METADATA)
                .build();

        String CONTENT_TYPE_ITEM = CONTENT_TYPE_ITEM_BASE + Tables.CATEGORY_METADATA;

        String CONTENT_TYPE_DIR = CONTENT_TYPE_DIR_BASE + Tables.CATEGORY_METADATA;
    }

}

package org.lagonette.android.database.statement;

import org.lagonette.android.content.contract.LaGonetteContract;

public class CategoryMetadataStatement {

    public static String getSelections() {
        return LaGonetteContract.CategoryMetadata.CATEGORY_ID + " = ?";
    }

    public static String[] getSelectionsArgs(long categoryId) {
        return new String[]{
                String.valueOf(categoryId)
        };
    }

}

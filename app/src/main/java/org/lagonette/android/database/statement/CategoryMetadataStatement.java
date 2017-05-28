package org.lagonette.android.database.statement;

import org.lagonette.android.content.contract.GonetteContract;

public class CategoryMetadataStatement {

    public static String getSelections() {
        return GonetteContract.CategoryMetadata.CATEGORY_ID + " = ?";
    }

    public static String[] getSelectionsArgs(long categoryId) {
        return new String[]{
                String.valueOf(categoryId)
        };
    }

}

package org.lagonette.app.util;

import java.util.HashSet;

public final class AdapterUtil {

    private static final String TAG = "AdapterUtil";

    private AdapterUtil() {
    }

    public static final long createItemId(int rowType, int rowTypeCount, long contentId) {
        long factor = rowType % 2 == 0 ? 1 : -1;
        return (contentId * factor * rowTypeCount) + ((rowType + 1) * factor / 2);
    }

    public static final long getContentId(int rowType, int rowTypeCount, long itemId) {
        long factor = rowType % 2 == 0 ? 1 : -1;
        return (itemId - ((rowType + 1) * factor / 2)) / (factor * rowTypeCount);
    }


    //TODO UNIT TESTING
    public static void main(String[] args) {
        int rowTypeCount = 50;
        int rowCount = 50;
        HashSet<Long> ids = new HashSet<>();
        for (int rowType = 0; rowType < rowTypeCount; rowType++) {
            for (int row = 0; row < rowCount; row++) {
                long adapterId = AdapterUtil.createItemId(rowType, rowTypeCount, row);
                long contentId = AdapterUtil.getContentId(rowType, rowTypeCount, adapterId);
                String prefix = " \t ";
                if (row != contentId) {
                    prefix += " - " + false + " - ";
                }
                System.out.print("\n rowType: " + rowType + " \t row: " + row + " \t adapterId: " + adapterId + " \t contentId: " + contentId + prefix);
                ids.add(adapterId);
            }
        }

        int expectedSize = (rowCount * rowTypeCount);
        if (ids.size() == expectedSize) {
            System.out.print("\n Success ? " + true);
        } else {
            System.out.print("\n Success ? " + false + " expected size: " + expectedSize + " - real size: " + ids.size());
        }
    }
}

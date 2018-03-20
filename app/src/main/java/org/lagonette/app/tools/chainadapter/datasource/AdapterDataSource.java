package org.lagonette.app.tools.chainadapter.datasource;

import android.support.annotation.Nullable;

public interface AdapterDataSource<Item> {

    long getItemId(int position);

    @Nullable
    Item getItem(int position);

    int getCount();
}
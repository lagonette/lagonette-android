package org.lagonette.app.tools.chainadapter.datasource;

import android.support.annotation.Nullable;

public class SingleDataSource<Item> implements AdapterDataSource<Item> {

    @Nullable
    private Item mItem;

    private boolean mForceDisplay;

    public SingleDataSource(boolean forceDisplay) {
        mForceDisplay = forceDisplay;
    }

    @Override
    public long getItemId(int position) {
        if (position > 0) {
            throw new IllegalStateException("Position should be equals to 0.");
        }

        return 0;
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        if (!mForceDisplay && mItem == null) {
            throw new IllegalStateException("Item is null, you should not get item.");
        }

        if (position > 0) {
            throw new IllegalStateException("Position should be equals to 0.");
        }

        return mItem;
    }

    @Override
    public int getCount() {
        return mForceDisplay || mItem != null ? 1 : 0;
    }

    public void setItem(@Nullable Item item) {
        mItem = item;
    }
}

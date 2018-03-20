package org.lagonette.app.tools.chainadapter.datasource;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ListDataSource<Item> implements AdapterDataSource<Item> {

    @Nullable
    private List<Item> mItems;

    @Override
    public long getItemId(int position) {
        Item item = getItem(position);
        if (item == null) {
            return RecyclerView.NO_ID;
        }
        else {
            return getItemId(item);
        }
    }

    protected abstract long getItemId(@NonNull Item item);

    @Nullable
    @Override
    public Item getItem(int position) {
        if (mItems != null) {
            int size = mItems.size();
            if (position > size) {
                throw new IllegalStateException("Position should not be greater than list size.");
            }
            return mItems.get(position);
        }
        else {
            throw new IllegalStateException("List is empty, you should not get item.");
        }

    }

    @Override
    public int getCount() {
        if (mItems != null) {
            return mItems.size();
        }
        else {
            return 0;
        }
    }

    public void setItems(@Nullable List<Item> items) {
        mItems = items;
    }
}

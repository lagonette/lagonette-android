package org.lagonette.app.tools.chainadapter.datasource;

import android.arch.paging.AsyncPagedListDiffer;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public abstract class PagedListDataSource<Item> implements AdapterDataSource<Item> {

    @NonNull
    private AsyncPagedListDiffer<Item> mDiffer;

    public PagedListDataSource(@NonNull RecyclerView.Adapter adapter) {
        mDiffer = createDiffer(adapter);
    }

    protected abstract AsyncPagedListDiffer<Item> createDiffer(@NonNull RecyclerView.Adapter adapter);

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

    @Override
    public Item getItem(int position) {
        return mDiffer.getItem(position);
    }

    @Override
    public int getCount() {
        return mDiffer.getItemCount();
    }

    public void setPagedList(@Nullable PagedList<Item> pagedList) {
        mDiffer.submitList(pagedList);
    }
}

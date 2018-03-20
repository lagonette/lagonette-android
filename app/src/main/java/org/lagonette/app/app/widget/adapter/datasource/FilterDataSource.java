package org.lagonette.app.app.widget.adapter.datasource;

import android.arch.paging.AsyncPagedListDiffer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.tools.chainadapter.datasource.PagedListDataSource;

public class FilterDataSource extends PagedListDataSource<Filter> {

    public FilterDataSource(@NonNull RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected AsyncPagedListDiffer<Filter> createDiffer(@NonNull RecyclerView.Adapter adapter) {
        return new AsyncPagedListDiffer<>(
                adapter,
                new Filter.DiffCallback() // TODO Instantiate only one time
        );
    }

    @Override
    protected long getItemId(@NonNull Filter filter) {
        return filter.categoryKey.getUniqueId(); // TODO Do it correctly
    }
}

package org.lagonette.app.app.widget.adapter.datasource;

import android.arch.paging.AsyncPagedListDiffer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.tools.chainadapter.datasource.PagedListDataSource;
import org.lagonette.app.tools.identifier.Identifier;

public class FilterDataSource
        extends PagedListDataSource<Filter> {

    @NonNull
    private final Identifier mIdentifier;

    private final int mCategoryType;

    private final int mLocationType;

    private final int mFooterType;

    public FilterDataSource(@NonNull RecyclerView.Adapter adapter, @NonNull Identifier identifier) {
        super(adapter);
        mIdentifier = identifier;
        mCategoryType = mIdentifier.addType();
        mLocationType = mIdentifier.addType();
        mFooterType = mIdentifier.addType();
    }

    @Override
    protected AsyncPagedListDiffer<Filter> createDiffer(@NonNull RecyclerView.Adapter adapter) {
        return new AsyncPagedListDiffer<>(
                adapter,
                Filter.DIFF_CALLBACK
        );
    }

    @Override
    protected long getItemId(int position, @NonNull Filter filter) {
        switch (filter.rowType) {
            case FilterStatement.VALUE_ROW_CATEGORY:
                return mIdentifier.gen(mCategoryType, filter.categoryKey.getUniqueId());
            case FilterStatement.VALUE_ROW_FOOTER:
                return mIdentifier.gen(mFooterType, filter.categoryKey.getUniqueId());
            default:
            case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                return mIdentifier.gen(mLocationType, filter.locationId);
        }
    }
}

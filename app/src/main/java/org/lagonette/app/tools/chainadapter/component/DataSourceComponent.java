package org.lagonette.app.tools.chainadapter.component;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lagonette.app.tools.chainadapter.datasource.AdapterDataSource;
import org.lagonette.app.tools.chainadapter.decorator.AdapterDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSourceComponent<VH extends RecyclerView.ViewHolder, I, S>
    implements AdapterComponent<VH> {

    @NonNull
    private final AdapterDataSource<I, S> mDataSource;

    @NonNull
    private final List<AdapterDecorator<? extends VH, I>> mDecorators;

    public DataSourceComponent(@NonNull AdapterDataSource<I, S> dataSource) {
        this.mDataSource = dataSource;
        mDecorators = new ArrayList<>();
    }

    public DataSourceComponent(@NonNull AdapterDataSource<I, S> dataSource, @NonNull AdapterDecorator<? extends VH, I>... decorators) {
        this.mDataSource = dataSource;
        this.mDecorators = Arrays.asList(decorators);
    }

    @Override
    public int getItemCount() {
        return mDataSource.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        I item = mDataSource.getItem(position);
        for (AdapterDecorator<? extends VH, I> decorator : mDecorators) {
            if (decorator.handleItem(item)) {
                return decorator.getViewType();
            }
        }
        throw new IllegalStateException("There no decorator to handle position " + position);
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.getItemId(position);
    }

    @NonNull
    @Override
    public VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
        for (AdapterDecorator<? extends VH, I> decorator : mDecorators) {
            if (decorator.handleViewType(viewType)) {
                return decorator.createViewHolder(parent);
            }
        }
        throw new IllegalStateException("There no decorator to handle view type " + viewType);
    }

    @Override
    public void bindViewHolder(@NonNull VH viewHolder, int position) {
        I item = mDataSource.getItem(position);
        for (AdapterDecorator<? extends VH, I> decorator : mDecorators) {
            if (decorator.handleItem(item)) {
                decorator.bindViewHolder(item, viewHolder);
                return;
            }
        }
        throw new IllegalStateException("There no decorator to handle position " + position);
    }

    public boolean handleViewType(int viewType) {
        for (AdapterDecorator<? extends VH, I> decorator : mDecorators) {
            if (decorator.handleViewType(viewType)) {
                return true;
            }
        }
        return false;
    }

    public void setSource(@Nullable S source) {
        mDataSource.setSource(source);
    }
}

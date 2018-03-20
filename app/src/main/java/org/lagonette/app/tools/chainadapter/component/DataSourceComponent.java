package org.lagonette.app.tools.chainadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lagonette.app.tools.chainadapter.datasource.AdapterDataSource;
import org.lagonette.app.tools.chainadapter.decorator.AdapterDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSourceComponent<VH extends RecyclerView.ViewHolder, I, DS extends AdapterDataSource<I>>
    implements AdapterComponent<VH> {

    @NonNull
    public final DS dataSource;

    @NonNull
    public final List<AdapterDecorator<? extends VH, I>> decorators;

    public DataSourceComponent(@NonNull DS dataSource) {
        this.dataSource = dataSource;
        decorators = new ArrayList<>();
    }

    public DataSourceComponent(@NonNull DS dataSource, @NonNull AdapterDecorator<? extends VH, I>... decorators) {
        this.dataSource = dataSource;
        this.decorators = Arrays.asList(decorators);
    }

    @Override
    public int getItemCount() {
        return dataSource.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        I item = dataSource.getItem(position);
        for (AdapterDecorator<? extends VH, I> decorator : decorators) {
            if (decorator.handleItem(item)) {
                return decorator.getViewType();
            }
        }
        throw new IllegalStateException("There no decorator to handle position " + position);
    }

    @Override
    public long getItemId(int position) {
        return dataSource.getItemId(position);
    }

    @NonNull
    @Override
    public VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
        for (AdapterDecorator<? extends VH, I> decorator : decorators) {
            if (decorator.handleViewType(viewType)) {
                return decorator.createViewHolder(parent);
            }
        }
        throw new IllegalStateException("There no decorator to handle view type " + viewType);
    }

    @Override
    public void bindViewHolder(@NonNull VH viewHolder, int position) {
        I item = dataSource.getItem(position);
        for (AdapterDecorator<? extends VH, I> decorator : decorators) {
            if (decorator.handleItem(item)) {
                decorator.bindViewHolder(item, viewHolder);
                return;
            }
        }
        throw new IllegalStateException("There no decorator to handle position " + position);
    }

    public boolean handleViewType(int viewType) {
        for (AdapterDecorator<? extends VH, I> decorator : decorators) {
            if (decorator.handleViewType(viewType)) {
                return true;
            }
        }
        return false;
    }
}
package org.lagonette.app.tools.chainadapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


public abstract class AbstractAdapterDecorator<VH extends RecyclerView.ViewHolder, I>
        implements AdapterDecorator<VH, I> {

    private final int mViewType;

    protected AbstractAdapterDecorator(int viewType) {
        mViewType = viewType;
    }

    public final void bindViewHolder(@Nullable I item, @NonNull RecyclerView.ViewHolder holder) {
        //noinspection unchecked
        onBindViewHolder(item, (VH) holder);
    }

    protected abstract void onBindViewHolder(@Nullable I item, @NonNull VH viewHolder);

    public final boolean handleViewType(int viewType) {
        return viewType == mViewType;
    }

    public final int getViewType() {
        return mViewType;
    }

    @Override
    public boolean handleItem(@Nullable I item) {
        return true;
    }
}

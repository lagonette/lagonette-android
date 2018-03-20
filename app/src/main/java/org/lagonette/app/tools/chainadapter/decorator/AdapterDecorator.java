package org.lagonette.app.tools.chainadapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface AdapterDecorator<ViewHolder extends RecyclerView.ViewHolder, Item> {

    @NonNull
    ViewHolder createViewHolder(@NonNull ViewGroup parent);

    void bindViewHolder(@Nullable Item item, @NonNull RecyclerView.ViewHolder holder);

    boolean handleViewType(int viewType);

    int getViewType();

    boolean handleItem(@Nullable Item item);
}
package org.lagonette.app.app.widget.adapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.LoadingViewHolder;
import org.lagonette.app.tools.chainadapter.decorator.VoidDecorator;

public class LoadingDecorator extends VoidDecorator<LoadingViewHolder> {

    public LoadingDecorator() {
        super(R.id.view_type_loading);
    }

    @NonNull
    @Override
    public LoadingViewHolder createViewHolder(@NonNull ViewGroup parent) {
        return new LoadingViewHolder(parent);
    }

    @Override
    protected void onBindViewHolder(@Nullable Void aVoid, @NonNull LoadingViewHolder viewHolder) {

    }
}

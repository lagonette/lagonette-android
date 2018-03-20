package org.lagonette.app.app.widget.adapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.VoidComponent;
import org.lagonette.app.tools.chainadapter.decorator.VoidDecorator;

public class BaseVoidComponent
        extends VoidComponent<RecyclerView.ViewHolder> {

    public BaseVoidComponent(int itemCount, @NonNull VoidDecorator<? extends RecyclerView.ViewHolder> decorator) {
        super(itemCount, decorator);
    }

    public BaseVoidComponent(@NonNull VoidDecorator<? extends RecyclerView.ViewHolder> decorator) {
        super(decorator);
    }
}

package org.lagonette.app.tools.chainadapter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.DataSourceComponent;
import org.lagonette.app.tools.chainadapter.datasource.AdapterDataSource;
import org.lagonette.app.tools.chainadapter.decorator.AdapterDecorator;

public abstract class DataSourceAdapter<VH extends RecyclerView.ViewHolder, I, DS extends AdapterDataSource<I>>
        extends ComponentAdapter<VH, DataSourceComponent<VH, I, DS>> {

    public void addDecorator(@NonNull AdapterDecorator<? extends VH, I> decorator) {
        DataSourceComponent<VH, I, DS> component = get();
        if (component != null) {
            component.addDecorator(decorator);
        }
        else {
            throw new IllegalStateException("You must provide a component before adding a decorator.");
        }
    }

}

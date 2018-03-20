package org.lagonette.app.app.widget.adapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.DataSourceComponent;
import org.lagonette.app.tools.chainadapter.datasource.AdapterDataSource;
import org.lagonette.app.tools.chainadapter.decorator.AdapterDecorator;

public class BaseComponent<I, DS extends AdapterDataSource<I>>
        extends DataSourceComponent<RecyclerView.ViewHolder, I, DS> {

    public BaseComponent(@NonNull DS dataSource) {
        super(dataSource);
    }

    public BaseComponent(@NonNull DS dataSource, @NonNull AdapterDecorator<? extends RecyclerView.ViewHolder, I>... decorators) {
        super(dataSource, decorators);
    }
}

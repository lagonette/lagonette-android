package org.lagonette.app.tools.chainadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.datasource.VoidDataSource;
import org.lagonette.app.tools.chainadapter.decorator.VoidDecorator;

public class VoidComponent<VH extends RecyclerView.ViewHolder>
        extends DataSourceComponent<VH,Void,VoidDataSource> {

    public VoidComponent(int itemCount, @NonNull VoidDecorator<? extends VH> decorator) {
        super(new VoidDataSource(itemCount));
        addDecorator(decorator);
    }

    public VoidComponent(@NonNull VoidDecorator<? extends VH> decorator) {
        super(new VoidDataSource(1));
        addDecorator(decorator);
    }
}

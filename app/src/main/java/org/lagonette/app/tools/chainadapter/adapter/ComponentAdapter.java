package org.lagonette.app.tools.chainadapter.adapter;

import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.AdapterComponent;

public abstract class ComponentAdapter<VH extends RecyclerView.ViewHolder, C extends AdapterComponent<VH>>
        extends DelegationAdapter<VH, C> {

}

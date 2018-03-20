package org.lagonette.app.tools.chainadapter.component;

import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.delegate.AdapterDelegate;

public interface AdapterComponent<VH extends RecyclerView.ViewHolder>
        extends AdapterDelegate<VH> {

    boolean handleViewType(int viewType);
}

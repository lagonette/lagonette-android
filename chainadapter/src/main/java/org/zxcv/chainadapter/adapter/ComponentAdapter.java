package org.zxcv.chainadapter.adapter;

import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.component.AdapterComponent;

public abstract class ComponentAdapter<VH extends RecyclerView.ViewHolder, C extends AdapterComponent<VH>>
		extends DelegationAdapter<VH, C> {

}

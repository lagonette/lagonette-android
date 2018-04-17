package org.zxcv.chainadapter.adapter;

import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.component.DataSourceComponent;

public abstract class DataSourceAdapter<VH extends RecyclerView.ViewHolder, I, S>
		extends ComponentAdapter<VH, DataSourceComponent<VH, I, S>> {

}

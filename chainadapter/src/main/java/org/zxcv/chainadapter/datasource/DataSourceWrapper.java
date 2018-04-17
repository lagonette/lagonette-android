package org.zxcv.chainadapter.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DataSourceWrapper<I, S>
		implements AdapterDataSource<I, S> {

	@NonNull
	protected final AdapterDataSource<I, S> mWrappedDataSource;

	public DataSourceWrapper(@NonNull AdapterDataSource<I, S> dataSource) {
		mWrappedDataSource = dataSource;
	}

	@Override
	public long getItemId(int position) {
		return mWrappedDataSource.getItemId(position);
	}

	@Nullable
	@Override
	public I getItem(int position) {
		return mWrappedDataSource.getItem(position);
	}

	@Override
	public int getCount() {
		return mWrappedDataSource.getCount();
	}

	@Override
	public void setSource(@Nullable S source) {
		mWrappedDataSource.setSource(source);
	}
}

package org.lagonette.app.app.widget.adapter.datasource.wrapper;

import android.support.annotation.NonNull;

import org.zxcv.chainadapter.datasource.AdapterDataSource;
import org.zxcv.chainadapter.datasource.DataSourceWrapper;
import org.zxcv.identifier.Identifier;


public class SingleIdentifierDataSourceWrapper<I, S>
		extends DataSourceWrapper<I, S> {

	@NonNull
	private final Identifier mIdentifier;

	private final int mIdentifierType;

	public SingleIdentifierDataSourceWrapper(
			@NonNull AdapterDataSource<I, S> dataSource,
			@NonNull Identifier identifier) {
		super(dataSource);
		mIdentifier = identifier;
		mIdentifierType = mIdentifier.addType();
	}

	@Override
	public long getItemId(int position) {
		return mIdentifier.gen(mIdentifierType, super.getItemId(position));
	}
}

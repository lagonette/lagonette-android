package org.lagonette.app.app.widget.adapter;

import android.arch.paging.PagedList;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.app.widget.adapter.datasource.FilterDataSource;
import org.lagonette.app.app.widget.adapter.datasource.wrapper.SingleIdentifierDataSourceWrapper;
import org.lagonette.app.app.widget.adapter.decorator.CategoryDecorator;
import org.lagonette.app.app.widget.adapter.decorator.FooterDecorator;
import org.lagonette.app.app.widget.adapter.decorator.LoadingDecorator;
import org.lagonette.app.app.widget.adapter.decorator.LocationDecorator;
import org.lagonette.app.app.widget.adapter.decorator.ShortcutDecorator;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.tools.chainadapter.adapter.ChainAdapter;
import org.lagonette.app.tools.chainadapter.component.DataSourceComponent;
import org.lagonette.app.tools.chainadapter.datasource.SingleItemDataSource;
import org.lagonette.app.tools.chainadapter.link.AdapterLink;
import org.lagonette.app.tools.chainadapter.locator.HeaderLocator;
import org.lagonette.app.tools.identifier.Identifier;

public class FilterAdapter
		extends ChainAdapter<RecyclerView.ViewHolder> {

	public final static int RANK_FILTERS = 0;

	public final static int RANK_LOADING = 1;

	public final static int RANK_SHORTCUT = 2;

	public final static int TYPE_COUNT = 5;

	@NonNull
	public final ShortcutDecorator.Callbacks shortcutCallbacks;

	@NonNull
	public final CategoryDecorator.Callbacks categoryCallbacks;

	@NonNull
	public final LocationDecorator.Callbacks locationCallbacks;

	@NonNull
	private final DataSourceComponent<RecyclerView.ViewHolder, Filter, PagedList<Filter>> mFilterComponent;

	@NonNull
	private final DataSourceComponent<RecyclerView.ViewHolder, HeadquarterShortcut, HeadquarterShortcut> mShortcutComponent;

	@NonNull
	private final Identifier mIdentifier;

	@NonNull
	private final AdapterLink<RecyclerView.ViewHolder> mLoadingLink;

	public FilterAdapter(@NonNull Context context, @NonNull Resources resources) {

		mIdentifier = new Identifier(TYPE_COUNT);

		shortcutCallbacks = new ShortcutDecorator.Callbacks();
		categoryCallbacks = new CategoryDecorator.Callbacks();
		locationCallbacks = new LocationDecorator.Callbacks();

		HeaderLocator<RecyclerView.ViewHolder> headerLocator = new HeaderLocator<>();

		mFilterComponent = new DataSourceComponent<>(
				new FilterDataSource(
						FilterAdapter.this,
						mIdentifier
				),
				new CategoryDecorator(
						resources,
						categoryCallbacks
				),
				new LocationDecorator(
						context,
						locationCallbacks
				),
				new FooterDecorator()
		);

		mShortcutComponent = new DataSourceComponent<>(
				new SingleIdentifierDataSourceWrapper<>(
						new SingleItemDataSource<>(true),
						mIdentifier
				),
				new ShortcutDecorator(
						context,
						resources,
						shortcutCallbacks
				)
		);

		mLoadingLink = chainUp(
				RANK_LOADING,
				new DataSourceComponent<>(
						new SingleIdentifierDataSourceWrapper<>(
								new SingleItemDataSource<>(true),
								mIdentifier
						),
						new LoadingDecorator()
				)
		);

		chainUp(
				RANK_FILTERS,
				mFilterComponent
		);

		chainUp(
				RANK_SHORTCUT,
				mShortcutComponent,
				headerLocator
		);
	}

	public void setFilters(@Nullable PagedList<Filter> filters) {
		// There is no need to notify adapter data set changed because PagedList already do this.
		mFilterComponent.setSource(filters);
		if (filters != null) {
			if (isChained(RANK_LOADING)) {
				unchain(RANK_LOADING);
			}
		}
		else {
			if (!isChained(RANK_LOADING)) {
				chainUp(mLoadingLink);
				notifyDataSetChanged();
			}
		}
	}

	public void setHeadquarterShortcut(@Nullable HeadquarterShortcut headquarterShortcut) {
		mShortcutComponent.setSource(headquarterShortcut);
		notifyDataSetChanged();
	}
}

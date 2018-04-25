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
import org.lagonette.app.app.widget.adapter.decorator.EmptyViewDecorator;
import org.lagonette.app.app.widget.adapter.decorator.FooterDecorator;
import org.lagonette.app.app.widget.adapter.decorator.LoadingDecorator;
import org.lagonette.app.app.widget.adapter.decorator.LocationDecorator;
import org.lagonette.app.app.widget.adapter.decorator.ShortcutDecorator;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.Shortcut;
import org.zxcv.chainadapter.adapter.ChainAdapter;
import org.zxcv.chainadapter.component.DataSourceComponent;
import org.zxcv.chainadapter.datasource.SingleItemDataSource;
import org.zxcv.chainadapter.link.AdapterLink;
import org.zxcv.chainadapter.locator.HeaderLocator;
import org.zxcv.identifier.Identifier;

public class FilterAdapter
		extends ChainAdapter<RecyclerView.ViewHolder> {

	public final static int RANK_FILTERS = 0;

	public final static int RANK_EMPTY_VIEW = 1;

	public final static int RANK_LOADING = 2;

	public final static int RANK_SHORTCUT = 3;

	public final static int TYPE_COUNT = 6;

	@NonNull
	public final ShortcutDecorator.Callbacks shortcutCallbacks;

	@NonNull
	public final CategoryDecorator.Callbacks categoryCallbacks;

	@NonNull
	public final LocationDecorator.Callbacks locationCallbacks;

	@NonNull
	private final DataSourceComponent<RecyclerView.ViewHolder, Filter, PagedList<Filter>> mFilterComponent;

	@NonNull
	private final DataSourceComponent<RecyclerView.ViewHolder, Shortcut, Shortcut> mShortcutComponent;

	@NonNull
	private final Identifier mIdentifier;

	@NonNull
	private final AdapterLink<RecyclerView.ViewHolder> mLoadingLink;

	@NonNull
	private final AdapterLink<RecyclerView.ViewHolder> mEmptyViewLink;

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

		mEmptyViewLink = chainUp(
				RANK_EMPTY_VIEW,
				new DataSourceComponent<>(
						new SingleIdentifierDataSourceWrapper<>(
								new SingleItemDataSource<>(true),
								mIdentifier
						),
						new EmptyViewDecorator()
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
			ensureLoadingLinkIsUnchained();

			if (filters.size() > 0) {
				ensureEmptyViewLinkIsUnchained();
			}
			else {
				ensureEmptyViewLinkIsChained();
			}
		}
	}

	public void ensureLoadingLinkIsUnchained() {
		if (isChained(RANK_LOADING)) {
			unchain(RANK_LOADING);
		}
	}

	public void ensureEmptyViewLinkIsUnchained() {
		if (isChained(RANK_EMPTY_VIEW)) {
			unchain(RANK_EMPTY_VIEW);
		}
	}

	public void ensureEmptyViewLinkIsChained() {
		if (!isChained(RANK_EMPTY_VIEW)) {
			unchain(RANK_EMPTY_VIEW);
		}
	}

	public void setShortcut(@Nullable Shortcut Shortcut) {
		mShortcutComponent.setSource(Shortcut);
		notifyDataSetChanged();
	}
}

package org.lagonette.app.app.widget.adapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.FooterViewHolder;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.tools.chainadapter.decorator.AbstractAdapterDecorator;

public class FooterDecorator
		extends AbstractAdapterDecorator<FooterViewHolder, Filter> {

	public FooterDecorator() {
		super(R.id.view_type_footer);
	}

	@NonNull
	@Override
	public FooterViewHolder createViewHolder(@NonNull ViewGroup parent) {
		return new FooterViewHolder(parent);
	}

	@Override
	public boolean handleItem(@Nullable Filter filter) {
		return filter != null && filter.rowType == FilterStatement.VALUE_ROW_FOOTER;
	}

	@Override
	protected void onBindViewHolder(@Nullable Filter filter, @NonNull FooterViewHolder viewHolder) {

	}

}

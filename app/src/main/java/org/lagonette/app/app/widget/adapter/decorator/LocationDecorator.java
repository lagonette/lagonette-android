package org.lagonette.app.app.widget.adapter.decorator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.LocationViewHolder;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.tools.chainadapter.decorator.AbstractAdapterDecorator;
import org.lagonette.app.tools.functions.main.LongBooleanConsumer;
import org.lagonette.app.tools.functions.main.LongConsumer;

public class LocationDecorator
        extends AbstractAdapterDecorator<LocationViewHolder, Filter> {

    public static class Callbacks {

        @Nullable
        public LongConsumer onClick = LongConsumer::doNothing;

        @Nullable
        public LongBooleanConsumer onVisibilityClick = LongBooleanConsumer::doNothing;

    }

    @NonNull
    private final Callbacks mCallbacks;

    @NonNull
    private final Drawable mPartnerIndicatorImage;

    @NonNull
    private final Drawable mExchangeOfficeIndicatorImage;

    public LocationDecorator(
            @NonNull Context context,
            @NonNull Callbacks callbacks) {
        super(R.id.view_type_location);

        mCallbacks = callbacks;

        mPartnerIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_partner_indicator);
        mExchangeOfficeIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_exchange_office_indicator);
    }

    @NonNull
    @Override
    public LocationViewHolder createViewHolder(@NonNull ViewGroup parent) {
        LocationViewHolder holder = new LocationViewHolder(parent);

        if (mCallbacks.onClick != null) {
            holder.itemView.setOnClickListener(view -> mCallbacks.onClick.accept(holder.locationId));
        }

        if (mCallbacks.onVisibilityClick != null) {
            holder.visibilityButton.setOnClickListener(view -> mCallbacks.onVisibilityClick.accept(holder.locationId, !holder.isVisible));
        }

        return holder;
    }

    @Override
    protected void onBindViewHolder(@Nullable Filter filter, @NonNull LocationViewHolder holder) {
        if (filter != null) {
            holder.locationId = filter.locationId;
            holder.isVisible = filter.isLocationVisible;
            holder.isCategoryVisible = filter.isCategoryVisible;
            holder.isExchangeOffice = filter.isLocationExchangeOffice;
            holder.isMainPartner = filter.rowType == FilterStatement.VALUE_ROW_MAIN_PARTNER;

            holder.nameTextView.setText(filter.partnerName);
            holder.itemView.setClickable(holder.isVisible);

            String address = filter.address.format(holder.itemView.getResources());
            if (!TextUtils.isEmpty(address)) {
                holder.addressTextView.setText(address);
                holder.addressTextView.setVisibility(View.VISIBLE);
            } else {
                holder.addressTextView.setVisibility(View.GONE);
            }

            if (holder.isVisible && holder.isCategoryVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_grey_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }

            if (holder.isExchangeOffice) {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mExchangeOfficeIndicatorImage);
            } else {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mPartnerIndicatorImage);
            }
        }
    }

    @Override
    public boolean handleItem(@Nullable Filter filter) {
        return filter != null && filter.rowType == FilterStatement.VALUE_ROW_MAIN_PARTNER;
    }
}

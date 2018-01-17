package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.app.R;
import org.lagonette.app.tools.functions.LongBooleanConsumer;
import org.lagonette.app.tools.functions.LongConsumer;

public class LocationViewHolder extends RecyclerView.ViewHolder {

    public long locationId;

    public boolean isVisible;

    public boolean isCategoryVisible;

    public boolean isMainPartner;

    public boolean isExchangeOffice;

    @NonNull
    public final TextView nameTextView;

    @NonNull
    public final TextView addressTextView;

    @NonNull
    public final ImageView exchangeOfficeIndicatorImage;

    @NonNull
    public final ImageButton visibilityButton;

    public LocationViewHolder(
            @NonNull ViewGroup parent,
            @Nullable LongConsumer onLocationClick,
            @Nullable LongBooleanConsumer onVisibilityClick) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_location,
                                parent,
                                false
                        )
        );
        nameTextView = itemView.findViewById(R.id.location_name);
        addressTextView = itemView.findViewById(R.id.location_address);
        exchangeOfficeIndicatorImage = itemView.findViewById(R.id.location_exchange_office_indicator);
        visibilityButton = itemView.findViewById(R.id.location_visibility);

        if (onLocationClick != null) {
            itemView.setOnClickListener(view -> onLocationClick.accept(locationId));
        }

        if (onVisibilityClick != null) {
            visibilityButton.setOnClickListener(view -> onVisibilityClick.accept(locationId, !isVisible));
        }
    }
}
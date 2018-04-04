package org.lagonette.app.room.entity.statement;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class LocationItem
        implements ClusterItem {

    @ColumnInfo(name = "id")
    private final long mId;

    @NonNull
    @ColumnInfo(name = "position")
    private final LatLng mPosition;

    @ColumnInfo(name = "icon")
    @NonNull
    private final String mIconUrl;

    @ColumnInfo(name = "is_exchange_office")
    private final boolean mIsExchangeOffice;

    @ColumnInfo(name = "is_gonette_headquarter")
    private final boolean mIsGonetteHeadquarter;

    @ColumnInfo(name = "main_category_id")
    private final long mCategoryId;

    public LocationItem(
            long id,
            long categoryId,
            boolean isGonetteHeadquarter,
            boolean isExchangeOffice,
            @NonNull LatLng position,
            @NonNull String iconUrl) {
        mId = id;
        mIsGonetteHeadquarter = isGonetteHeadquarter;
        mIsExchangeOffice = isExchangeOffice;
        mPosition = position;
        mCategoryId = categoryId;
        mIconUrl = iconUrl;
    }

    public long getId() {
        return mId;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    public boolean isExchangeOffice() {
        return mIsExchangeOffice;
    }

    @NonNull
    public String getIconUrl() {
        return mIconUrl;
    }

    @NonNull
    public long getCategoryId() {
        return mCategoryId;
    }

    public boolean isGonetteHeadquarter() {
        return mIsGonetteHeadquarter;
    }

    public boolean displayAsExchangeOffice() {
        return mIsExchangeOffice && !mIsGonetteHeadquarter;
    }
}

package org.lagonette.android.app.widget.maps;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.lagonette.android.content.reader.PartnerReader;

public class PartnerItem
        implements ClusterItem {

    @NonNull
    private final LatLng mPosition;

    @NonNull
    private final String mIconUrl;

    @Nullable
    private String mTitle;

    @Nullable
    private String mSnippet;

    private long mId;

    private boolean mIsExchangeOffice;

    private long mCategoryId;

    public PartnerItem(@NonNull PartnerReader partnerReader) {
        this(
                partnerReader.getId(),
                partnerReader.getLatitude(),
                partnerReader.getLongitude(),
                partnerReader.isExchangeOffice(),
                partnerReader.getMainCategoryId(),
                partnerReader.categoryReader.getIcon()
        );
    }

    public PartnerItem(long id, double lat, double lng, boolean isExchangeOffice, long categoryId, @NonNull String iconUrl) {
        mId = id;
        mIsExchangeOffice = isExchangeOffice;
        mPosition = new LatLng(lat, lng);
        mCategoryId = categoryId;
        mIconUrl = iconUrl;
        mTitle = null;
        mSnippet = null;
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
        return mTitle;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return mSnippet;
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
}

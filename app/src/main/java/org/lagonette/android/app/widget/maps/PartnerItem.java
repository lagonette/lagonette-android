package org.lagonette.android.app.widget.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PartnerItem
        implements ClusterItem {

    private final LatLng mPosition;

    private String mTitle;

    private String mSnippet;

    private long mId;

    private boolean mIsExchangeOffice;

    public PartnerItem(long id, double lat, double lng, boolean isExchangeOffice) {
        mId = id;
        mIsExchangeOffice = isExchangeOffice;
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public long getId() {
        return mId;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public boolean isExchangeOffice() {
        return mIsExchangeOffice;
    }
}

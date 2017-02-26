package com.zxcv.gonette.app.ui.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PartnerItem
        implements ClusterItem {

    private final LatLng mPosition;

    private String mTitle;

    private String mSnippet;

    private long mId;

    public PartnerItem(long id, double lat, double lng) {
        mId = id;
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public PartnerItem(long id, double lat, double lng, String title, String snippet) {
        mId = id;
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
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

    /**
     * Set the title of the marker
     *
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     *
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }
}

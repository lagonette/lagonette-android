package org.lagonette.app.app.widget.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.MarkerOptions;

import org.lagonette.app.room.entity.statement.LocationItem;

public class PartnerMarkerTarget extends SimpleTarget<Bitmap> {

    @NonNull
    private final Callback mCallback;

    @NonNull
    private final LocationItem mLocationItem;

    @NonNull
    private final MarkerOptions mMarkerOptions;

    public interface Callback {

        void onItemBitmapReady(@NonNull LocationItem locationItem, @NonNull MarkerOptions markerOptions, @NonNull Bitmap bitmap);

    }

    public PartnerMarkerTarget(
            @NonNull Callback callback,
            @NonNull LocationItem locationItem,
            @NonNull MarkerOptions markerOptions,
            int width,
            int height) {
        super(width, height);
        mCallback = callback;
        mLocationItem = locationItem;
        mMarkerOptions = markerOptions;
    }

    @Override
    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        mCallback.onItemBitmapReady(mLocationItem, mMarkerOptions, bitmap);
    }

}

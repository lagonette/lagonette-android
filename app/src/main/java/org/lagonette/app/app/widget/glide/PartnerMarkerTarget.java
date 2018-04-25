package org.lagonette.app.app.widget.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.MarkerOptions;

import org.lagonette.app.room.entity.statement.LocationItem;
import org.zxcv.functions.main.TriConsumer;

public class PartnerMarkerTarget
		extends SimpleTarget<Bitmap> {

	@NonNull
	private final TriConsumer<LocationItem, MarkerOptions, Bitmap> mOnItemBitmapReady;

	@NonNull
	private final LocationItem mLocationItem;

	@NonNull
	private final MarkerOptions mMarkerOptions;

	public PartnerMarkerTarget(
			@NonNull LocationItem locationItem,
			@NonNull MarkerOptions markerOptions,
			@NonNull TriConsumer<LocationItem, MarkerOptions, Bitmap> onItemBitmapReady,
			int width,
			int height) {
		super(width, height);
		mLocationItem = locationItem;
		mMarkerOptions = markerOptions;
		mOnItemBitmapReady = onItemBitmapReady;
	}

	@Override
	public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
		mOnItemBitmapReady.accept(mLocationItem, mMarkerOptions, bitmap);
	}

}

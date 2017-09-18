package org.lagonette.app.app.widget.maps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.glide.PartnerMarkerTarget;

public class PartnerRenderer
        extends DefaultClusterRenderer<PartnerItem> implements PartnerMarkerTarget.Callback {

    private static final String TAG = "PartnerRenderer";

    public static final int MIN_CLUSTER_SIZE = 7;

    private final IconGenerator mClusterIconGenerator;

    private final BitmapDescriptor mPartnerPlaceholderBitmapDescriptor;

    private final BitmapDescriptor mExchangeOfficePlaceholderBitmapDescriptor;

    @NonNull
    private final Context mContext;

    private final int mItemSize;

    @NonNull
    private final IconGenerator mIconGenerator;

    @NonNull
    private final View mPartnerView;

    @NonNull
    private final ImageView mPartnerIconView;

    @NonNull
    private final Drawable mPartnerBackgroundDrawable;

    @NonNull
    private final Drawable mExchangeOfficeBackgroundDrawable;

    @NonNull
    private SparseArray<BitmapDescriptor> mPartnerBitmapDescriptors;

    @NonNull
    private SparseArray<BitmapDescriptor> mExchangeOfficeBitmapDescriptors;

    public PartnerRenderer(
            @NonNull Context context,
            @NonNull LayoutInflater layoutInflater,
            @NonNull GoogleMap map,
            @NonNull ClusterManager<PartnerItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;

        mPartnerBitmapDescriptors = new SparseArray<>();
        mExchangeOfficeBitmapDescriptors = new SparseArray<>();

        Resources resources = context.getResources();
        mItemSize = resources.getDimensionPixelSize(R.dimen.map_item_size);

        Context appContext = context.getApplicationContext();

        mIconGenerator = new IconGenerator(appContext);
        mClusterIconGenerator = new IconGenerator(appContext);

        View clusterView = layoutInflater.inflate(R.layout.item_cluster, null);
        mClusterIconGenerator.setContentView(clusterView);
        mClusterIconGenerator.setBackground(null);

        mPartnerView = layoutInflater.inflate(R.layout.item_partner, null);
        mPartnerIconView = mPartnerView.findViewById(R.id.item_partner_icon);

        mPartnerBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_item_partner);
        mPartnerView.setBackground(mPartnerBackgroundDrawable);
        mIconGenerator.setContentView(mPartnerView);
        mIconGenerator.setBackground(null);
        mPartnerPlaceholderBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());

        mExchangeOfficeBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_item_exchange_office);
        mPartnerView.setBackground(mExchangeOfficeBackgroundDrawable);
        mIconGenerator.setContentView(mPartnerView);
        mIconGenerator.setBackground(null);
        mExchangeOfficePlaceholderBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());

        setMinClusterSize(MIN_CLUSTER_SIZE);
    }

    // TODO Display just a point instead of logo when zoom is huge

    @Override
    protected void onBeforeClusterItemRendered(
            final PartnerItem partnerItem,
            final MarkerOptions markerOptions) {

        boolean isExchangeOffice = partnerItem.isExchangeOffice();

        markerOptions
                .icon( // TODO Make a correct placeholder
                        isExchangeOffice
                                ? mExchangeOfficePlaceholderBitmapDescriptor
                                : mPartnerPlaceholderBitmapDescriptor
                )
                .anchor(0.5f, 0.5f)
                .flat(true)
                .title(partnerItem.getTitle());

        int categoryId = (int) partnerItem.getCategoryId();
        BitmapDescriptor bitmapDescriptor = isExchangeOffice
                ? mExchangeOfficeBitmapDescriptors.get(categoryId)
                : mPartnerBitmapDescriptors.get(categoryId);

        if (bitmapDescriptor != null) {
            markerOptions.icon(bitmapDescriptor);
        } else {
            Glide
                    .with(mContext)
                    .load(partnerItem.getIconUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(
                            new PartnerMarkerTarget(
                                    PartnerRenderer.this,
                                    partnerItem,
                                    markerOptions,
                                    mItemSize,
                                    mItemSize
                            )
                    );
        }

    }

    @Override
    protected void onBeforeClusterRendered(
            Cluster<PartnerItem> cluster,
            MarkerOptions markerOptions) {
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 0.5f)
                .flat(true);
    }

    // TODO Check, remake, improve and so on

    @Override
    public void onItemBitmapReady(
            @NonNull PartnerItem partnerItem,
            @NonNull MarkerOptions markerOptions,
            @NonNull Bitmap bitmap) {

        boolean isExchangeOffice = partnerItem.isExchangeOffice();
        int categoryId = (int) partnerItem.getCategoryId();
        SparseArray<BitmapDescriptor> bitmapDescriptors = isExchangeOffice
                ? mExchangeOfficeBitmapDescriptors
                : mPartnerBitmapDescriptors;
        BitmapDescriptor bitmapDescriptor = bitmapDescriptors.get(categoryId);

        if (bitmapDescriptor == null) { // TODO Not tested yet.
            mPartnerIconView.setImageBitmap(bitmap);
            mPartnerView.setBackground(
                    partnerItem.isExchangeOffice()
                            ? mExchangeOfficeBackgroundDrawable
                            : mPartnerBackgroundDrawable
            );
            mIconGenerator.setContentView(mPartnerView);
            mIconGenerator.setBackground(null);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());
            bitmapDescriptors.append(categoryId, bitmapDescriptor);
        }

        Marker marker = getMarker(partnerItem);
        if (marker != null) {
            marker.setIcon(bitmapDescriptor);
        } else {
            markerOptions.icon(bitmapDescriptor);
        }
    }

}

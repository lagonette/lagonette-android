package org.lagonette.android.app.widget.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.lagonette.android.R;

public class PartnerRenderer
        extends DefaultClusterRenderer<PartnerItem> {

    public static final int MIN_CLUSTER_SIZE = 7;

    private final IconGenerator mClusterIconGenerator;

    private final BitmapDescriptor mPartnerBitmapDescriptor;

    private final BitmapDescriptor mExchangeOfficeBitmapDescriptor;

    public PartnerRenderer(
            Context context,
            LayoutInflater layoutInflater,
            GoogleMap map,
            ClusterManager<PartnerItem> clusterManager) {
        super(context, map, clusterManager);
        Context appContext = context.getApplicationContext();

        IconGenerator mIconGenerator = new IconGenerator(appContext);
        mClusterIconGenerator = new IconGenerator(appContext);

        View clusterView = layoutInflater.inflate(R.layout.item_cluster, null);
        mClusterIconGenerator.setContentView(clusterView);
        mClusterIconGenerator.setBackground(null);

        View partnerView = layoutInflater.inflate(R.layout.item_partner, null);
        mIconGenerator.setContentView(partnerView);
        mIconGenerator.setBackground(null);
        mPartnerBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());

        View exchangePartnerView = layoutInflater.inflate(R.layout.item_exchange_office, null);
        mIconGenerator.setContentView(exchangePartnerView);
        mIconGenerator.setBackground(null);
        mExchangeOfficeBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());

        setMinClusterSize(MIN_CLUSTER_SIZE);
    }

    @Override
    protected void onBeforeClusterItemRendered(
            PartnerItem partnerItem,
            MarkerOptions markerOptions) {
        markerOptions.icon(partnerItem.isExchangeOffice()
                ? mExchangeOfficeBitmapDescriptor
                : mPartnerBitmapDescriptor)
                .anchor(0.5f, 0.5f)
                .flat(true)
                .title(partnerItem.getTitle());
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

}

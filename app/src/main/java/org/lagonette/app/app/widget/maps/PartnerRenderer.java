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
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.functions.NullableSupplier;
import org.lagonette.app.tools.functions.TriConsumer;

public class PartnerRenderer
        extends DefaultClusterRenderer<LocationItem> {

    private static final String TAG = "PartnerRenderer";

    public static final float Z_INDEX_ITEM = 0f;

    public static final float Z_INDEX_SELECTED_ITEM = 1f;

    public static final int MIN_CLUSTER_SIZE = 7;

    private final IconGenerator mClusterIconGenerator;

    private final BitmapDescriptor mPartnerPlaceholderBitmapDescriptor;

    private final BitmapDescriptor mExchangeOfficePlaceholderBitmapDescriptor;

    private final BitmapDescriptor mSelectedPartnerPlaceholderBitmapDescriptor;

    private final BitmapDescriptor mSelectedExchangeOfficePlaceholderBitmapDescriptor;

    @NonNull
    private final Context mContext;

    private final int mItemSize;

    @NonNull
    private final IconGenerator mIconGenerator;

    @NonNull
    private final View mPartnerView;

    @NonNull
    private final View mSelectedPartnerView;

    @NonNull
    private final ImageView mPartnerIconView;

    @NonNull
    private final ImageView mSelectedPartnerIconView;

    @NonNull
    private final Drawable mPartnerBackgroundDrawable;

    @NonNull
    private final Drawable mExchangeOfficeBackgroundDrawable;

    @NonNull
    private final Drawable mSelectedPartnerBackgroundDrawable;

    @NonNull
    private final Drawable mSelectedExchangeOfficeBackgroundDrawable;

    @NonNull
    private SparseArray<BitmapDescriptor> mPartnerBitmapDescriptors;

    @NonNull
    private SparseArray<BitmapDescriptor> mExchangeOfficeBitmapDescriptors;

    @NonNull
    private SparseArray<BitmapDescriptor> mSelectedPartnerBitmapDescriptors;

    @NonNull
    private SparseArray<BitmapDescriptor> mSelectedExchangeOfficeBitmapDescriptors;

    @NonNull
    private final NullableSupplier<Marker> mGetSelectedItem;

    public PartnerRenderer(
            @NonNull Context context,
            @NonNull LayoutInflater layoutInflater,
            @NonNull GoogleMap map,
            @NonNull ClusterManager<LocationItem> clusterManager,
            @NonNull NullableSupplier<Marker> getSelectedItem) {
        super(context, map, clusterManager);
        mContext = context;

        mGetSelectedItem = getSelectedItem;

        mPartnerBitmapDescriptors = new SparseArray<>();
        mExchangeOfficeBitmapDescriptors = new SparseArray<>();
        mSelectedPartnerBitmapDescriptors = new SparseArray<>();
        mSelectedExchangeOfficeBitmapDescriptors = new SparseArray<>();

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
        mPartnerPlaceholderBitmapDescriptor = createBitmapDescriptor(mPartnerView);

        mExchangeOfficeBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_item_exchange_office);
        mPartnerView.setBackground(mExchangeOfficeBackgroundDrawable);
        mExchangeOfficePlaceholderBitmapDescriptor = createBitmapDescriptor(mPartnerView);


        mSelectedPartnerView = layoutInflater.inflate(R.layout.item_partner_selected, null);
        mSelectedPartnerIconView = mSelectedPartnerView.findViewById(R.id.item_partner_icon);

        mSelectedPartnerBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_item_partner);
        mSelectedPartnerView.setBackground(mSelectedPartnerBackgroundDrawable);
        mSelectedPartnerPlaceholderBitmapDescriptor = createBitmapDescriptor(mSelectedPartnerView);

        mSelectedExchangeOfficeBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_item_exchange_office);
        mSelectedPartnerView.setBackground(mSelectedExchangeOfficeBackgroundDrawable);
        mSelectedExchangeOfficePlaceholderBitmapDescriptor = createBitmapDescriptor(mSelectedPartnerView);


        setMinClusterSize(MIN_CLUSTER_SIZE);
    }

    private BitmapDescriptor createBitmapDescriptor(@NonNull View contentView) {
        mIconGenerator.setContentView(contentView);
        mIconGenerator.setBackground(null);
        return BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon());
    }

    //TODO Display just a point instead of logo when zoom is huge

    @Override
    protected void onBeforeClusterItemRendered(
            final LocationItem locationItem,
            final MarkerOptions markerOptions) {
        render(locationItem, markerOptions, this::onItemBitmapReady, false);
    }

    public void render(
            @NonNull final LocationItem locationItem,
            @NonNull final MarkerOptions markerOptions,
            @NonNull final TriConsumer<LocationItem, MarkerOptions, Bitmap> onItemBitmapReady,
            boolean isSelectedItem) {

        boolean displayAsExchangeOffice = locationItem.displayAsExchangeOffice();

        markerOptions
                .icon( //TODO Make a correct placeholder
                        getPlaceholderBitmapDescriptor(displayAsExchangeOffice, isSelectedItem)
                )
                .anchor(0.5f, 0.5f)
                .flat(true)
                .title(locationItem.getTitle())
                .zIndex(isSelectedItem ? Z_INDEX_SELECTED_ITEM : Z_INDEX_ITEM);

        int categoryId = (int) locationItem.getCategoryId();
        SparseArray<BitmapDescriptor> bitmapDescriptors = getBitmapDescriptors(displayAsExchangeOffice, isSelectedItem);
        BitmapDescriptor bitmapDescriptor = bitmapDescriptors.get(categoryId);

        if (bitmapDescriptor != null) {
            markerOptions.icon(bitmapDescriptor);
        } else {
            Glide
                    .with(mContext)
                    .load(locationItem.getIconUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(
                            new PartnerMarkerTarget(
                                    locationItem,
                                    markerOptions,
                                    onItemBitmapReady,
                                    mItemSize,
                                    mItemSize
                            )
                    );
        }
    }

    @Override
    protected void onBeforeClusterRendered(
            Cluster<LocationItem> cluster,
            MarkerOptions markerOptions) {
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 0.5f)
                .flat(true);
    }

    //TODO Check, remake, improve and so on

    public void onItemBitmapReady(
            @NonNull LocationItem locationItem,
            @NonNull MarkerOptions markerOptions,
            @NonNull Bitmap bitmap) {

        // Load bitmap descriptor for selected item
        getBitmapDescriptor(locationItem, bitmap, true);

        // Load and get bitmap descriptor for current item
        BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(locationItem, bitmap, false);

        Marker marker = getMarker(locationItem);
        if (marker != null) {
            marker.setIcon(bitmapDescriptor);
        } else {
            markerOptions.icon(bitmapDescriptor);
        }
    }

    public void onSelectedItemBitmapReady(
            @NonNull LocationItem locationItem,
            @NonNull MarkerOptions markerOptions,
            @NonNull Bitmap bitmap) {

        BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(locationItem, bitmap, true);

        Marker marker = mGetSelectedItem.get();
        if (marker != null) {
            marker.setIcon(bitmapDescriptor);
        } else {
            markerOptions.icon(bitmapDescriptor);
        }
    }

    @NonNull
    private BitmapDescriptor getBitmapDescriptor(
            @NonNull LocationItem locationItem,
            @NonNull Bitmap bitmap,
            boolean isSelectedItem) {
        boolean displayAsExchangeOffice = locationItem.displayAsExchangeOffice();
        int categoryId = (int) locationItem.getCategoryId();
        SparseArray<BitmapDescriptor> bitmapDescriptors = getBitmapDescriptors(displayAsExchangeOffice, isSelectedItem);
        BitmapDescriptor bitmapDescriptor = bitmapDescriptors.get(categoryId);

        if (bitmapDescriptor == null) {
            View partnerView = getPartnerView(isSelectedItem);
            ImageView partnerIconView = getPartnerIconView(isSelectedItem);
            partnerIconView.setImageBitmap(bitmap);
            partnerView.setBackground(getItemDrawable(displayAsExchangeOffice, isSelectedItem));
            bitmapDescriptor = createBitmapDescriptor(partnerView);
            bitmapDescriptors.append(categoryId, bitmapDescriptor);
        }

        return bitmapDescriptor;
    }

    @NonNull
    private BitmapDescriptor getPlaceholderBitmapDescriptor(boolean displayAsExchangeOffice, boolean isSelectedItem) {
        if (displayAsExchangeOffice && isSelectedItem) {
            return mSelectedExchangeOfficePlaceholderBitmapDescriptor;
        }
        else if (isSelectedItem) {
            return mSelectedPartnerPlaceholderBitmapDescriptor;
        }
        else if (displayAsExchangeOffice) {
            return mExchangeOfficePlaceholderBitmapDescriptor;
        }
        else {
            return mPartnerPlaceholderBitmapDescriptor;
        }
    }

    @NonNull
    private SparseArray<BitmapDescriptor> getBitmapDescriptors(boolean displayAsExchangeOffice, boolean isSelectedItem) {
        if (displayAsExchangeOffice && isSelectedItem) {
            return mSelectedExchangeOfficeBitmapDescriptors;
        }
        else if (isSelectedItem) {
            return mSelectedPartnerBitmapDescriptors;
        }
        else if (displayAsExchangeOffice) {
            return mExchangeOfficeBitmapDescriptors;
        }
        else {
            return mPartnerBitmapDescriptors;
        }
    }

    @NonNull
    private Drawable getItemDrawable(boolean displayAsExchangeOffice, boolean isSelectedItem) {
        if (displayAsExchangeOffice && isSelectedItem) {
            return mSelectedExchangeOfficeBackgroundDrawable;
        }
        else if (isSelectedItem) {
            return mSelectedPartnerBackgroundDrawable;
        }
        else if (displayAsExchangeOffice) {
            return mExchangeOfficeBackgroundDrawable;
        }
        else {
            return mPartnerBackgroundDrawable;
        }
    }

    @NonNull
    private View getPartnerView(boolean isSelectedItem) {
        if (isSelectedItem) {
            return mSelectedPartnerView;
        }
        else {
            return mPartnerView;
        }
    }

    @NonNull
    private ImageView getPartnerIconView(boolean isSelectedItem) {
        if (isSelectedItem) {
            return mSelectedPartnerIconView;
        }
        else {
            return mPartnerIconView;
        }
    }

    public MarkerOptions createSelectedMarkerOptions(@NonNull LocationItem locationItem) {
        MarkerOptions selectedOptions = new MarkerOptions()
                .position(locationItem.getPosition());
        render(locationItem, selectedOptions, this::onSelectedItemBitmapReady, true);
        return selectedOptions;
    }

}

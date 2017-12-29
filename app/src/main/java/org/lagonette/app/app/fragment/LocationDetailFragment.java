package org.lagonette.app.app.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.LocationDetailViewModel;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.IntentUtil;
import org.lagonette.app.util.PhoneUtil;
import org.lagonette.app.util.SnackbarUtil;

import java.util.Locale;

public class LocationDetailFragment
        extends SlideableFragment
        implements View.OnClickListener {

    public static final String TAG = "LocationDetailFragment";

    private static final String ARG_LOCATION_ID = "arg:location_id";

    @NonNull
    public static LocationDetailFragment newInstance(long locationId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_LOCATION_ID, locationId);
        LocationDetailFragment locationDetailFragment = new LocationDetailFragment();
        locationDetailFragment.setArguments(args);
        return locationDetailFragment;
    }

    private LocationDetailViewModel mViewModel;

    private View mHeaderView;

    private TextView mNameTextView;

//    private ImageButton mBackImageButton;

    private TextView mPartnerTypeTextView;

    private TextView mShortDescriptionTextView;

    private TextView mDescriptionTextView;

    private View mAddressLayout;

    private TextView mAddressTextView;

    private View mPhoneLayout;

    private TextView mPhoneTextView;

    private View mWebsiteLayout;

    private TextView mWebsiteTextView;

    private View mEmailLayout;

    private TextView mEmailTextView;

    private View mOpeningHoursLayout;

    private TextView mOpeningHoursTextView;

    private TextView mMainCategoryLabelTextView;

//    private ImageView mLogoImageView;

//    private ImageView mMainCategoryLogoImageView;

    private double mLatitude;

    private double mLongitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders
                .of(LocationDetailFragment.this)
                .get(LocationDetailViewModel.class);

        //TODO Check
        if (savedInstanceState == null) {
            long locationId = getArguments().getLong(ARG_LOCATION_ID, Statement.NO_ID);
            mViewModel.setLocationId(locationId);
        }

        mViewModel.getLocationDetail().observe(
                LocationDetailFragment.this,
                this::dispatchLocationDetailResource
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        mBackImageButton = view.findViewById(R.id.back);

        mHeaderView = view.findViewById(R.id.fragment_header);

        mPartnerTypeTextView = view.findViewById(R.id.type_partner);

        mNameTextView = view.findViewById(R.id.name);
        mShortDescriptionTextView = view.findViewById(R.id.short_description);
        mDescriptionTextView = view.findViewById(R.id.description);

        mAddressTextView = view.findViewById(R.id.address);
        mAddressLayout = view.findViewById(R.id.container_address);

        mPhoneTextView = view.findViewById(R.id.phone);
        mPhoneLayout = view.findViewById(R.id.container_phone);

        mWebsiteTextView = view.findViewById(R.id.website);
        mWebsiteLayout = view.findViewById(R.id.container_website);

        mEmailTextView = view.findViewById(R.id.email);
        mEmailLayout = view.findViewById(R.id.container_email);

        mOpeningHoursTextView = view.findViewById(R.id.opening_hours);
        mOpeningHoursLayout = view.findViewById(R.id.container_opening_hours);

        mMainCategoryLabelTextView = view.findViewById(R.id.main_category_label);
//        mLogoImageView = view.findViewById(R.id.logo);
//        mMainCategoryLogoImageView = view.findViewById(R.id.main_category_logo);

//        mBackImageButton.setOnClickListener(LocationDetailFragment.this);
        mAddressLayout.setOnClickListener(LocationDetailFragment.this);
        mPhoneLayout.setOnClickListener(LocationDetailFragment.this);
        mWebsiteLayout.setOnClickListener(LocationDetailFragment.this);
        mEmailLayout.setOnClickListener(LocationDetailFragment.this);
    }

    private void dispatchLocationDetailResource(@NonNull Resource<LocationDetail> resource) {
        switch (resource.status) {

            case Resource.LOADING:
                displayLocation(resource.data);
                //TODO
                break;

            case Resource.SUCCESS:
                displayLocation(resource.data);
                break;

            case Resource.ERROR:
                displayLocation(resource.data);
                //TODO
                break;
        }
    }

    private void displayLocation(@Nullable LocationDetail locationDetail) {
        if (locationDetail != null) {
            Resources resources = getResources();

            mLatitude = locationDetail.latitude;
            mLongitude = locationDetail.longitude;

            mPartnerTypeTextView.setText(
                    locationDetail.isExchangeOffice
                            ? resources.getString(R.string.partner_type_exchange_office)
                            : resources.getString(R.string.partner_type_partner)
            );

            mNameTextView.setText(locationDetail.name);
            mShortDescriptionTextView.setText(locationDetail.shortDescription);
            mDescriptionTextView.setText(locationDetail.description);

            // OPENING HOURS
            String openingHours = locationDetail.openingHours;
            if (!TextUtils.isEmpty(openingHours)) {
                mOpeningHoursLayout.setVisibility(View.VISIBLE);
                mOpeningHoursTextView.setText(openingHours);
            } else {
                mOpeningHoursLayout.setVisibility(View.GONE);
            }

            // ADDRESS
            String address = locationDetail.address.format(resources);
            if (!TextUtils.isEmpty(address)) {
                mAddressLayout.setVisibility(View.VISIBLE);
                mAddressTextView.setText(address);
            } else {
                mAddressLayout.setVisibility(View.GONE);
            }

            // PHONE
            String phone = locationDetail.phone;
            if (!TextUtils.isEmpty(phone)) {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneTextView.setText(PhoneUtil.format(phone));
            } else {
                mPhoneLayout.setVisibility(View.GONE);
            }

            // WEBSITE
            String website = locationDetail.website;
            if (!TextUtils.isEmpty(website)) {
                mWebsiteLayout.setVisibility(View.VISIBLE);
                mWebsiteTextView.setText(website);
            } else {
                mWebsiteLayout.setVisibility(View.GONE);
            }

            // WEBSITE
            String email = locationDetail.email;
            if (!TextUtils.isEmpty(email)) {
                mEmailLayout.setVisibility(View.VISIBLE);
                mEmailTextView.setText(email);
            } else {
                mEmailLayout.setVisibility(View.GONE);
            }

            mMainCategoryLabelTextView.setText(locationDetail.label);

//            Glide.with(getContext())
//                    .load(reader.getLogo())
//                    .asBitmap()
//                    .into(mLogoImageView);

//            Glide.with(getContext())
//                    .load(reader.getCategoryIcon())
//                    .asBitmap()
//                    .into(mMainCategoryLogoImageView);
        }
    }

    //TODO factorize with MapsFragment
    // Maybe it is to the activity to manage that
    public void errorNoDirectionAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_direction_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoCallAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_call_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoBrowserAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_browser_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoEmailAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_email_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_address:
                onAddressClick();
                break;
            case R.id.container_phone:
                onPhoneClick();
                break;
            case R.id.container_website:
                onWebsiteClick();
                break;
            case R.id.container_email:
                onEmailClick();
                break;
//            case R.id.back:
//                getActivity().onBackPressed();
//                break;
        }
    }

    public void onAddressClick() {
        boolean success = IntentUtil.startDirection(
                getContext(),
                mLatitude,
                mLongitude
        );
        if (!success) {
            errorNoDirectionAppFound();
        }
    }

    public void onPhoneClick() {
        boolean success = IntentUtil.makeCall(
                getContext(),
                mPhoneTextView.getText().toString()
        );
        if (!success) {
            errorNoCallAppFound();
        }
    }

    public void onWebsiteClick() {
        boolean success = IntentUtil.goToWebsite(
                getContext(),
                mWebsiteTextView.getText().toString()
        );
        if (!success) {
            errorNoBrowserAppFound();
        }
    }

    public void onEmailClick() {
        boolean success = IntentUtil.writeEmail(
                getContext(),
                mEmailTextView.getText().toString()
        );
        if (!success) {
            errorNoEmailAppFound();
        }
    }

    @Override
    public void updateTopPadding(int top) {
        mHeaderView.setPadding(0, top, 0, 0);
    }

}

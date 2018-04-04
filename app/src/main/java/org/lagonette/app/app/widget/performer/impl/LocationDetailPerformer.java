package org.lagonette.app.app.widget.performer.impl;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.tools.functions.main.ObjBiDoubleConsumer;
import org.lagonette.app.util.PhoneUtils;

public class LocationDetailPerformer implements ViewPerformer {

    @NonNull
    public ObjBiDoubleConsumer<String> onAddressClick = ObjBiDoubleConsumer::doNothing;

    @NonNull
    public Consumer<String> onPhoneClick = Consumer::doNothing;

    @NonNull
    public Consumer<String> onWebsiteClick = Consumer::doNothing;

    @NonNull
    public Consumer<String> onEmailClick = Consumer::doNothing;

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
    public void inject(@NonNull View view) {
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
        mAddressLayout.setOnClickListener(v -> onAddressClick.accept(mNameTextView.getText().toString(), mLatitude, mLongitude));
        mPhoneLayout.setOnClickListener(v -> onPhoneClick.accept(mPhoneTextView.getText().toString()));
        mWebsiteLayout.setOnClickListener(v -> onWebsiteClick.accept(mWebsiteTextView.getText().toString()));
        mEmailLayout.setOnClickListener(v -> onEmailClick.accept(mEmailTextView.getText().toString()));
    }

    public void displayLocation(@Nullable LocationDetail locationDetail) {
        if (locationDetail != null) {
            Resources resources = mHeaderView.getResources();

            mLatitude = locationDetail.latitude;
            mLongitude = locationDetail.longitude;

            mPartnerTypeTextView.setText(
                    locationDetail.isExchangeOffice
                            ? resources.getString(R.string.location_detail_exchange_office)
                            : resources.getString(R.string.location_detail_partner)
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
                mPhoneTextView.setText(PhoneUtils.format(phone));
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

    public void updateTopPadding(int top) {
        mHeaderView.setPadding(0, top, 0, 0);
    }

}

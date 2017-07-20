package org.lagonette.android.app.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.presenter.PartnerDetailPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.util.DisplayUtil;
import org.lagonette.android.util.SnackbarUtil;

public class PartnerDetailFragment
        extends Fragment
        implements PartnerDetailContract.View, View.OnClickListener {

    public static final String TAG = "PartnerDetailContract";

    @NonNull
    private PartnerDetailPresenter mPresenter;

    private TextView mNameTextView;

    private ImageButton mBackImageButton;

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

    private ImageView mLogoImageView;

    private ImageView mMainCategoryLogoImageView;

    private double mLatitude;

    private double mLongitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new PartnerDetailPresenter(PartnerDetailFragment.this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBackImageButton = (ImageButton) view.findViewById(R.id.back);

        mPartnerTypeTextView = (TextView) view.findViewById(R.id.type_partner);

        mNameTextView = (TextView) view.findViewById(R.id.name);
        mShortDescriptionTextView = (TextView) view.findViewById(R.id.short_description);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);

        mAddressTextView = (TextView) view.findViewById(R.id.address);
        mAddressLayout = view.findViewById(R.id.container_address);

        mPhoneTextView = (TextView) view.findViewById(R.id.phone);
        mPhoneLayout = view.findViewById(R.id.container_phone);

        mWebsiteTextView = (TextView) view.findViewById(R.id.website);
        mWebsiteLayout = view.findViewById(R.id.container_website);

        mEmailTextView = (TextView) view.findViewById(R.id.email);
        mEmailLayout = view.findViewById(R.id.container_email);

        mOpeningHoursTextView = (TextView) view.findViewById(R.id.opening_hours);
        mOpeningHoursLayout = view.findViewById(R.id.container_opening_hours);

        mMainCategoryLabelTextView = (TextView) view.findViewById(R.id.main_category_label);
        mLogoImageView = (ImageView) view.findViewById(R.id.logo);
        mMainCategoryLogoImageView = (ImageView) view.findViewById(R.id.main_category_logo);

        mBackImageButton.setOnClickListener(PartnerDetailFragment.this);
        mAddressLayout.setOnClickListener(PartnerDetailFragment.this);
        mPhoneLayout.setOnClickListener(PartnerDetailFragment.this);
        mWebsiteLayout.setOnClickListener(PartnerDetailFragment.this);
        mEmailLayout.setOnClickListener(PartnerDetailFragment.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void displayPartner(@Nullable PartnerReader reader) {
        if (reader != null && reader.moveToFirst()) {
            Resources resources = getResources();

            mLatitude = reader.getLatitude();
            mLongitude = reader.getLongitude();

            mPartnerTypeTextView.setText(
                    reader.isExchangeOffice()
                            ? getString(R.string.partner_type_exchange_office)
                            : getString(R.string.partner_type_partner)
            );

            mNameTextView.setText(reader.getName());
            mShortDescriptionTextView.setText(reader.getShortDescription());
            mDescriptionTextView.setText(reader.getDescription());

            // OPENING HOURS
            String openingHours = reader.getOpeningHours();
            if (!TextUtils.isEmpty(openingHours)) {
                mOpeningHoursLayout.setVisibility(View.VISIBLE);
                mOpeningHoursTextView.setText(openingHours);
            } else {
                mOpeningHoursLayout.setVisibility(View.GONE);
            }

            // ADDRESS
            String address = reader.getAddress();
            String zipCode = reader.getZipCode();
            String city = reader.getCity();
            if (!TextUtils.isEmpty(address)
                    && !TextUtils.isEmpty(zipCode)
                    && !TextUtils.isEmpty(city)) {
                mAddressLayout.setVisibility(View.VISIBLE);
                mAddressTextView.setText(
                        DisplayUtil.formatAddress(
                                resources,
                                address,
                                zipCode,
                                city
                        )
                );
            } else {
                mAddressLayout.setVisibility(View.GONE);
            }

            // PHONE
            String phone = reader.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneTextView.setText(phone);
            } else {
                mPhoneLayout.setVisibility(View.GONE);
            }

            // WEBSITE
            String website = reader.getWebsite();
            if (!TextUtils.isEmpty(website)) {
                mWebsiteLayout.setVisibility(View.VISIBLE);
                mWebsiteTextView.setText(website);
            } else {
                mWebsiteLayout.setVisibility(View.GONE);
            }

            // WEBSITE
            String email = reader.getEmail();
            if (!TextUtils.isEmpty(email)) {
                mEmailLayout.setVisibility(View.VISIBLE);
                mEmailTextView.setText(email);
            } else {
                mEmailLayout.setVisibility(View.GONE);
            }

            mMainCategoryLabelTextView.setText(reader.categoryReader.getLabel());

            Glide.with(getContext())
                    .load(reader.getLogo())
                    .asBitmap()
                    .into(mLogoImageView);

            Glide.with(getContext())
                    .load(reader.categoryReader.getIcon())
                    .asBitmap()
                    .into(mMainCategoryLogoImageView);
        }
    }

    @NonNull
    @Override
    public String[] getPartnerDetailColumns() {
        return new String[]{
                LaGonetteContract.Partner.ID,
                LaGonetteContract.Partner.NAME,
                LaGonetteContract.Partner.DESCRIPTION,
                LaGonetteContract.Partner.SHORT_DESCRIPTION,
                LaGonetteContract.Partner.LATITUDE,
                LaGonetteContract.Partner.LONGITUDE,
                LaGonetteContract.Partner.ADDRESS,
                LaGonetteContract.Partner.CITY,
                LaGonetteContract.Partner.ZIP_CODE,
                LaGonetteContract.Partner.EMAIL,
                LaGonetteContract.Partner.WEBSITE,
                LaGonetteContract.Partner.PHONE,
                LaGonetteContract.Partner.OPENING_HOURS,
                LaGonetteContract.Partner.LOGO,
                LaGonetteContract.Partner.IS_EXCHANGE_OFFICE,
                LaGonetteContract.Category.LABEL,
                LaGonetteContract.Category.ICON
        };
    }

    // TODO factorize with MapsFragment
    // Maybe it is to the activity to manage that
    @Override
    public void errorNoDirectionAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_direction_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    @Override
    public void errorNoCallAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_call_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    @Override
    public void errorNoBrowserAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_browser_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    @Override
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
            case R.id.back:
                getActivity().onBackPressed();
                break;
        }
    }

    private void onAddressClick() {
        mPresenter.startDirection(mLatitude, mLongitude);
    }

    private void onPhoneClick() {
        mPresenter.makeCall((String) mPhoneTextView.getText());
    }

    private void onWebsiteClick() {
        mPresenter.goToWebsite((String) mWebsiteTextView.getText());
    }

    private void onEmailClick() {
        mPresenter.writeEmail((String) mEmailTextView.getText());
    }

}

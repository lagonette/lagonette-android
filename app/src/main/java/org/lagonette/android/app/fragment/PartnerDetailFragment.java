package org.lagonette.android.app.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.presenter.PartnerDetailPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.util.DisplayUtil;

public class PartnerDetailFragment
        extends Fragment
        implements PartnerDetailContract.View, View.OnClickListener {

    public static final String TAG = "PartnerDetailContract";

    @NonNull
    private PartnerDetailPresenter mPresenter;

    private TextView mNameTextView;

    private TextView mTypeTextView;

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
        mNameTextView = (TextView) view.findViewById(R.id.name);
        mTypeTextView = (TextView) view.findViewById(R.id.partner_type);
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

            mNameTextView.setText(reader.getName());
            mTypeTextView.setText(
                    reader.isExchangeOffice()
                            ? getString(R.string.partner_type_exchange_office)
                            : getString(R.string.partner_type_partner)
            );
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
        }
    }

    private void onAddressClick() {
        Toast.makeText(getContext(), "Address", Toast.LENGTH_SHORT).show();
    }

    private void onPhoneClick() {
        Toast.makeText(getContext(), "Phone", Toast.LENGTH_SHORT).show();
    }

    private void onWebsiteClick() {
        Toast.makeText(getContext(), "Website", Toast.LENGTH_SHORT).show();
    }

    private void onEmailClick() {
        Toast.makeText(getContext(), "Email", Toast.LENGTH_SHORT).show();
    }

}

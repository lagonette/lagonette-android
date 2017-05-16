package org.lagonette.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.presenter.PartnerDetailPresenter;
import org.lagonette.android.content.reader.PartnerReader;

public class PartnerDetailFragment
        extends Fragment
        implements PartnerDetailContract.View {

    public static final String TAG = "PartnerDetailContract";

    @NonNull
    private PartnerDetailPresenter mPresenter;

    private TextView mNameTextView;

    private TextView mDescriptionTextView;

    private TextView mShortDescriptionTextView;

    private TextView mAddressTextView;

    private TextView mCityTextView;

    private TextView mZipCodeTextView;

    private TextView mPhoneTextView;

    private TextView mWebsiteTextView;

    private TextView mEmailTextView;

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
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mShortDescriptionTextView = (TextView) view.findViewById(R.id.short_description);
        mAddressTextView = (TextView) view.findViewById(R.id.address);
        mCityTextView = (TextView) view.findViewById(R.id.city);
        mZipCodeTextView = (TextView) view.findViewById(R.id.zip_code);
        mPhoneTextView = (TextView) view.findViewById(R.id.phone);
        mWebsiteTextView = (TextView) view.findViewById(R.id.website);
        mEmailTextView = (TextView) view.findViewById(R.id.email);
        mOpeningHoursTextView = (TextView) view.findViewById(R.id.opening_hours);
        mMainCategoryLabelTextView = (TextView) view.findViewById(R.id.main_category_label);
        mLogoImageView = (ImageView) view.findViewById(R.id.logo);
        mMainCategoryLogoImageView = (ImageView) view.findViewById(R.id.main_category_logo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void displayPartner(@Nullable PartnerReader reader) {
        if (reader != null && reader.moveToFirst()) {
            mNameTextView.setText(reader.getName());
            mDescriptionTextView.setText(reader.getDescription());
            mShortDescriptionTextView.setText(reader.getShortDescription());
            mAddressTextView.setText(reader.getAddress());
            mCityTextView.setText(reader.getCity());
            mZipCodeTextView.setText(reader.getZipCode());
            mPhoneTextView.setText(reader.getPhone());
            mWebsiteTextView.setText(reader.getWebsite());
            mEmailTextView.setText(reader.getEmail());
            mOpeningHoursTextView.setText(reader.getOpeningHours());
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

}

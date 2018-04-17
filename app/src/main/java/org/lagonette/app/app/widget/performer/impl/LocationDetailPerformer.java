package org.lagonette.app.app.widget.performer.impl;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.tools.functions.main.ObjBiDoubleConsumer;
import org.lagonette.app.util.PhoneUtils;

public class LocationDetailPerformer
		implements ViewPerformer {

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

	private View mCategoriesLayout;

	private TextView mMainCategoryLabelTextView;

	private ImageView mMainCategoryLogoImageView;

	private View mMainCategoryLogoLayout;

	private double mLatitude;

	private double mLongitude;


	@Override
	public void inject(@NonNull View view) {

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
		mCategoriesLayout = view.findViewById(R.id.container_categories);
		mMainCategoryLogoImageView = view.findViewById(R.id.main_category_logo);
		mMainCategoryLogoLayout = view.findViewById(R.id.main_category_logo_container);

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
			if (!TextUtils.isEmpty(locationDetail.openingHours)) {
				mOpeningHoursLayout.setVisibility(View.VISIBLE);
				mOpeningHoursTextView.setText(locationDetail.openingHours);
			}
			else {
				mOpeningHoursLayout.setVisibility(View.GONE);
			}

			// ADDRESS
			String address = locationDetail.address.format(resources);
			if (!TextUtils.isEmpty(address)) {
				mAddressLayout.setVisibility(View.VISIBLE);
				mAddressTextView.setText(address);
			}
			else {
				mAddressLayout.setVisibility(View.GONE);
			}

			// PHONE
			if (!TextUtils.isEmpty(locationDetail.phone)) {
				mPhoneLayout.setVisibility(View.VISIBLE);
				mPhoneTextView.setText(PhoneUtils.format(locationDetail.phone));
			}
			else {
				mPhoneLayout.setVisibility(View.GONE);
			}

			// WEBSITE
			if (!TextUtils.isEmpty(locationDetail.website)) {
				mWebsiteLayout.setVisibility(View.VISIBLE);
				mWebsiteTextView.setText(locationDetail.website);
			}
			else {
				mWebsiteLayout.setVisibility(View.GONE);
			}

			// EMAIL
			if (!TextUtils.isEmpty(locationDetail.email)) {
				mEmailLayout.setVisibility(View.VISIBLE);
				mEmailTextView.setText(locationDetail.email);
			}
			else {
				mEmailLayout.setVisibility(View.GONE);
			}

			// CATEGORY
			if (locationDetail.mainCategoryId == Statement.NO_ID) {
				mCategoriesLayout.setVisibility(View.GONE);
			}
			else {
				mCategoriesLayout.setVisibility(View.VISIBLE);
				mMainCategoryLabelTextView.setText(locationDetail.label);

				Glide.with(mMainCategoryLogoImageView.getContext())
						.load(locationDetail.mainCategoryIcon)
						.asBitmap()
						.placeholder(R.drawable.img_item_default)
						.into(mMainCategoryLogoImageView);

				if (locationDetail.isExchangeOffice) {
					mMainCategoryLogoLayout.setBackgroundResource(R.drawable.bg_item_exchange_office);
				}
				else {
					mMainCategoryLogoLayout.setBackgroundResource(R.drawable.bg_item_partner);
				}
			}
		}
	}

	public void updateTopPadding(int top) {
		mHeaderView.setPadding(0, top, 0, 0);
	}

}

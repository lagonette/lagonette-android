package com.gonette.android.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gonette.android.app.contract.base.BaseContract;
import com.gonette.android.content.reader.PartnerReader;
import com.gonette.android.content.reader.PartnersVisibilityReader;

public abstract class FiltersContract {

    public interface Presenter extends BaseContract.BasePresenter {

        void setPartnerVisibility(long partnerId, boolean visibility);

        void setPartnersVisibility(boolean visibility);

        void filterPartner(@NonNull String search);

    }

    public interface View extends BaseContract.BaseView {

        void displayPartnersVisibility(@Nullable PartnersVisibilityReader visibilityReader);

        void displayPartners(@Nullable PartnerReader partnerReader);

        void resetPartnersVisibility();

        void resetPartners();

    }

}

package com.zxcv.gonette.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zxcv.gonette.app.contract.base.BaseContract;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

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

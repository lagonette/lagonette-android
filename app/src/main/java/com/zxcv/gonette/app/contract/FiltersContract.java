package com.zxcv.gonette.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public abstract class FiltersContract {

    public interface Presenter extends Contract.Presenter {

        void setPartnerVisibility(long partnerId, boolean visibility);

        void setPartnersVisibility(boolean visibility);

        void filterPartner(@NonNull String search);

    }

    public interface Fragment extends Contract.BaseView<Presenter> {

        void displayPartnersVisibility(@Nullable PartnersVisibilityReader visibilityReader);

        void displayPartners(@Nullable PartnerReader partnerReader);

        void resetPartnersVisibility();

        void resetPartners();

    }

}

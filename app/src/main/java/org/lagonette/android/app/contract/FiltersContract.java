package org.lagonette.android.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.content.reader.PartnersVisibilityReader;

public abstract class FiltersContract {

    public interface Presenter extends BaseContract.BasePresenter {

        void setPartnerVisibility(long partnerId, boolean visibility);

        void setPartnersVisibility(boolean visibility);

        void filterPartners(@NonNull String search);

        void LoadFilters();

    }

    public interface View extends BaseContract.BaseView {

        void displayPartnersVisibility(@Nullable PartnersVisibilityReader visibilityReader);

        void displayPartners(@Nullable PartnerReader partnerReader);

        void resetPartnersVisibility();

        void resetPartners();

    }

}

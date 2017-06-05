package org.lagonette.android.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.content.reader.FilterReader;

public abstract class FiltersContract {

    public interface Presenter extends BaseContract.BasePresenter {

        void setPartnerVisibility(long partnerId, boolean isVisible);

        void setCategoryVisibility(long categoryId, boolean isVisible);

        void setCategoryCollapsed(long categoryId, boolean isCollapsed);

        void filterPartners(@NonNull String search);

        void loadFilters();
    }

    public interface View extends BaseContract.BaseView {

        void displayFilters(@Nullable FilterReader filterReader);

        @NonNull
        String[] getFiltersColumns();
    }

}

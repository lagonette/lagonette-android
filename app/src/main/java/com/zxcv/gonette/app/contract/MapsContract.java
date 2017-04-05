package com.zxcv.gonette.app.contract;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zxcv.gonette.app.contract.base.BaseContract;
import com.zxcv.gonette.content.reader.PartnerReader;

public class MapsContract {

    public interface Presenter extends BaseContract.BasePresenter {

        void loadPartners();

        void loadPartners(@NonNull String search);

    }

    public interface View extends BaseContract.BaseView {

        void showPartners(@Nullable PartnerReader partnerReader);

        void errorNoDirectionAppFound();

    }
}

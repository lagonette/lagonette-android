package org.lagonette.android.app.contract;

import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.content.reader.PartnerReader;

public class PartnerDetailContract {

    public interface Presenter extends BaseContract.BasePresenter {

    }

    public interface View extends BaseContract.BaseView {

        void displayPartner(@Nullable PartnerReader reader);
    }

}

package org.lagonette.android.app.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.room.reader.PartnerDetailReader;

public class PartnerDetailContract {

    public interface Presenter extends BaseContract.BasePresenter {

        void startDirection(double latitude, double longitude);

        void makeCall(@NonNull String phoneNumber);

        void goToWebsite(@NonNull String url);

        void writeEmail(@NonNull String email);

    }

    public interface View extends BaseContract.BaseView {

        void displayPartner(@Nullable PartnerDetailReader reader);

        void errorNoDirectionAppFound();

        void errorNoCallAppFound();

        void errorNoBrowserAppFound();

        void errorNoEmailAppFound();

    }

}

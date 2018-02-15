package org.lagonette.app.tools.arch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class LocationViewModel extends AndroidViewModel {

    @NonNull
    private final LocationLiveData mLocationLiveData;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        mLocationLiveData = new LocationLiveData(application);
    }

    @NonNull
    public LocationLiveData getLocation() {
        return mLocationLiveData;
    }
}

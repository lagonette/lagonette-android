package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.app.widget.livedata.MainActionLiveData;
import org.lagonette.app.app.widget.livedata.MainStateLiveData;
import org.lagonette.app.app.widget.livedata.MainStatefulActionLiveData;
import org.lagonette.app.repo.Resource;

public class StateMapActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Integer> mWorkStatus;

    @NonNull
    private final MainActionLiveData mMainActionLiveData;

    @NonNull
    private final MainStateLiveData mMainStateLiveData;

    @NonNull
    private final MainStatefulActionLiveData mMainStatefulActionLiveData;

    @NonNull
    private final BottomSheetFragmentTypeLiveData mBottomSheetFragmentTypeLiveData;

    public StateMapActivityViewModel(Application application) {
        super(application);
        mMainStatefulActionLiveData = new MainStatefulActionLiveData();
        mBottomSheetFragmentTypeLiveData = new BottomSheetFragmentTypeLiveData();
        mMainActionLiveData = new MainActionLiveData(new MainAction());
        mMainStateLiveData = new MainStateLiveData(new MainState(mBottomSheetFragmentTypeLiveData.getNone()));
        mSearch = new MutableLiveData<>();
        mWorkStatus = new MutableLiveData<>();

        mMainStatefulActionLiveData.addSource(
                mMainActionLiveData,
                action -> mMainStatefulActionLiveData.setAction(action)
        );
        mMainStatefulActionLiveData.addSource(
                mMainStateLiveData,
                state -> mMainStatefulActionLiveData.setState(state)
        );
        mMainStatefulActionLiveData.addSource(
                mBottomSheetFragmentTypeLiveData,
                mMainStateLiveData::notifyBottomSheetFragmentChanged
        );

        mSearch.setValue("");
    }

    @NonNull
    public MainActionLiveData getMainActionLiveData() {
        return mMainActionLiveData;
    }

    @NonNull
    public MainStateLiveData getMainStateLiveData() {
        return mMainStateLiveData;
    }

    @NonNull
    public BottomSheetFragmentTypeLiveData getBottomSheetFragmentType() {
        return mBottomSheetFragmentTypeLiveData;
    }

    @NonNull
    public LiveData<MainStatefulAction> getMainStatefulActionLiveData() {
        return mMainStatefulActionLiveData;
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Integer> getWorkStatus() {
        return mWorkStatus;
    }

    public void setWorkStatus(@Resource.Status int status) {
        mWorkStatus.setValue(status);
    }
}

package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;

public class MainStatefulActionLiveData extends MediatorLiveData<MainStatefulAction> {

    //TODO Fix @NonNull and add final
    @NonNull
    private MainStatefulAction mStatefulAction;

    public void setAction(MainAction action) {
        mStatefulAction.action = action;
        setValue(mStatefulAction);
    }

    public void setState(MainState state) {
        mStatefulAction.state = state;
        setValue(mStatefulAction);
    }
}

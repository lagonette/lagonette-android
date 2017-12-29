package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;

public class MainStatefulActionLiveData extends MediatorLiveData<MainStatefulAction> {

    @NonNull
    private final MainStatefulAction mStatefulAction;

    public MainStatefulActionLiveData(@NonNull MainStatefulAction statefulAction) {
        mStatefulAction = statefulAction;
        setValue(mStatefulAction);
    }

    @NonNull
    @Override
    public MainStatefulAction getValue() {
        //noinspection ConstantConditions
        return super.getValue();
    }

    public void setAction(MainAction action) {
        mStatefulAction.action = action;
        setValue(mStatefulAction);
    }

    public void setState(MainState state) {
        mStatefulAction.state = state;
        setValue(mStatefulAction);
    }
}

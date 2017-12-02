package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.NonNull;

public class MainStatefulAction {

    @NonNull
    public MainAction action;

    @NonNull
    public MainState state;

    public MainStatefulAction(@NonNull MainAction action, @NonNull MainState state) {
        this.action = action;
        this.state = state;
    }
}

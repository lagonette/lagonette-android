package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainState;

public interface MainCoordinator {

    void init();

    void restore();

    void process(@NonNull MainState state);
}

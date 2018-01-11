package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;

public interface MainCoordinator {

    interface DoneMarker {

        void markPendingActionAsDone();

    }

    void init();

    void restore();

    void process(@NonNull MainAction action);
}

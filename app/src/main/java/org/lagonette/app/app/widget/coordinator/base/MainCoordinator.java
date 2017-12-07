package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;

public interface MainCoordinator {

    interface DoneMarker {

        void markPendingActionAsDone();

    }

    boolean back(@NonNull MainStatefulAction statefulAction);

    void process(@NonNull MainStatefulAction statefulAction);
}

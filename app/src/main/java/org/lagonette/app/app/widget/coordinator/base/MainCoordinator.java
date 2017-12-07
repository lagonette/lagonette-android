package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;

public interface MainCoordinator {

    interface BottomSheetCallback {

        void closeBottomSheet();

        void openBottomSheet();

    }

    interface FragmentLoader {

        void loadFiltersFragment();

        void loadLocationFragment(long locationId, boolean animation);

        void unloadFragment();

    }

    interface DoneMarker {

        void markPendingActionAsDone();

    }

    boolean back(@NonNull MainStatefulAction statefulAction);

    void process(@NonNull MainStatefulAction statefulAction);
}

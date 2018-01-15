package org.lagonette.app.app.widget.coordinator.command;

import android.support.annotation.NonNull;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.LongConsumer;
import org.lagonette.app.tools.functions.NullFunctions;

public class MainCommands {

    @NonNull
    public Consumer<Cluster<LocationItem>> moveMapToCluster = NullFunctions::accept;

    @NonNull
    public Consumer<LocationItem> moveMapToLocation = NullFunctions::accept;

    @NonNull
    public Runnable moveMapToFootprint = NullFunctions::run;

    @NonNull
    public Runnable stopMapMoving = NullFunctions::run;

    @NonNull
    public Consumer<MainAction> nextAction = NullFunctions::accept;

    @NonNull
    public Runnable openBottomSheet = NullFunctions::run;

    @NonNull
    public Runnable closeBottomSheet = NullFunctions::run;

    @NonNull
    public Runnable loadFilters = NullFunctions::run;

    @NonNull
    public Runnable unloadFilters = NullFunctions::run;

    @NonNull
    public LongConsumer loadLocationDetail = NullFunctions::accept;

    @NonNull
    public Runnable unloadLocationDetail = NullFunctions::run;

}

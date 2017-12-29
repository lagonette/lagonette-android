package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.Nullable;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.PartnerItem;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.IDLE;

public class MainAction {

    public enum  ActionType {
        IDLE,
        BACK,
        OPEN_FILTERS,
        MOVE_TO_MY_LOCATION,
        MOVE_TO_FOOTPRINT,
        MOVE_TO_CLUSTER,
        MOVE_TO_LOCATION,
        SHOW_FULL_MAP
    }

    public ActionType type;

    @Nullable
    public Cluster<PartnerItem> cluster;

    @Nullable
    public PartnerItem item;

    public boolean shouldMove;

    public MainAction() {
        type = IDLE;
        cluster = null;
        item = null;
        shouldMove = false;
    }

    public void done() {
        type = IDLE;
        cluster = null;
        item = null;
        shouldMove = false;
    }
}

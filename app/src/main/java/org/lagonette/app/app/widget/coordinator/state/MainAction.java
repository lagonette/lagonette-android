package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.Nullable;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.room.statement.Statement;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.IDLE;

public class MainAction {

    public enum  ActionType {
        IDLE,
        BACK,
        OPEN_FILTERS,
        MOVE_TO_MY_LOCATION,
        MOVE_TO_FOOTPRINT,
        MOVE_TO_CLUSTER,
        MOVE_TO_AND_OPEN_LOCATION,
        SHOW_FULL_MAP
    }

    public ActionType type;

    @Nullable
    public Cluster<PartnerItem> cluster;

    public long locationId;

    @Nullable
    public PartnerItem item;

    public boolean shouldMove;

    public MainAction() {
        done();
    }

    public void done() {
        type = IDLE;
        locationId = Statement.NO_ID;
        cluster = null;
        item = null;
        shouldMove = false;
    }
}

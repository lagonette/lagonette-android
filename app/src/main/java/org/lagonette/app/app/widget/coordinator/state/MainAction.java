package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.PartnerItem;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainAction {

    public static final int ACTION_IDLE = 0;

    public static final int ACTION_BACK = 1;

    public static final int ACTION_OPEN_FILTERS = 2;

    public static final int ACTION_MOVE_TO_MY_LOCATION = 3;

    public static final int ACTION_MOVE_TO_FOOTPRINT = 4;

    public static final int ACTION_MOVE_TO_CLUSTER = 5;

    public static final int ACTION_MOVE_TO_LOCATION = 6;

    public static final int ACTION_SHOW_FULL_MAP = 7;

    @Retention(SOURCE)
    @IntDef({
            ACTION_IDLE,
            ACTION_BACK,
            ACTION_OPEN_FILTERS,
            ACTION_MOVE_TO_MY_LOCATION,
            ACTION_MOVE_TO_FOOTPRINT,
            ACTION_MOVE_TO_CLUSTER,
            ACTION_MOVE_TO_LOCATION,
            ACTION_SHOW_FULL_MAP
    })
    public @interface ActionType {

    }

    @ActionType
    public int type;

    @Nullable
    public Cluster<PartnerItem> cluster;

    @Nullable
    public PartnerItem item;

    public boolean shouldMove;

    public MainAction() {
        type = ACTION_IDLE;
        cluster = null;
        item = null;
        shouldMove = false;
    }

    public void done() {
        type = ACTION_IDLE;
        cluster = null;
        item = null;
        shouldMove = false;
    }
}

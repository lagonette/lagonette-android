package org.lagonette.app.app.widget.adapter.datasource;

import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.tools.chainadapter.datasource.SingleDataSource;

public class ShortcutDataSource extends SingleDataSource<HeadquarterShortcut> {

    public ShortcutDataSource() {
        super(true);
    }
}

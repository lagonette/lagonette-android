package org.lagonette.app.tools.chainadapter.datasource;


import android.support.annotation.Nullable;

public abstract class AbstractDataSource<Item, Source> implements AdapterDataSource<Item, Source> {

    @Nullable
    protected Source mSource;

    @Override
    public void setSource(@Nullable Source source) {
        mSource = source;
    }
}

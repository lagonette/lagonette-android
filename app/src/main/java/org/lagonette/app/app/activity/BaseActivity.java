package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity
        extends AppCompatActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        onViewCreated();
        onActivityCreated(savedInstanceState);
    }

    protected abstract void setContentView();

    protected abstract void onViewCreated();

    protected abstract void onActivityCreated(Bundle savedInstanceState);
}

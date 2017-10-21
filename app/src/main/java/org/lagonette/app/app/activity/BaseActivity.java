package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity
        extends AppCompatActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        setContentView();
        onViewCreated(getWindow().getDecorView().getRootView());
        onActivityCreated(savedInstanceState);
    }

    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected abstract void setContentView();

    protected abstract void onViewCreated(@NonNull View view);

    protected abstract void onActivityCreated(@Nullable Bundle savedInstanceState);
}

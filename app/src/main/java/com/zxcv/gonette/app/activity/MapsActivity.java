package com.zxcv.gonette.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;

public class MapsActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState == null) {
            Fragment fragment = MapsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.content, fragment, MapsFragment.TAG)
                                       .commit();
        }
    }

}

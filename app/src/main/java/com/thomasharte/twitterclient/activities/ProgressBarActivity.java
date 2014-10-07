package com.thomasharte.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.fragments.StatusesFragment;

/**
 * Created by thomasharte on 06/10/2014.
 */
public class ProgressBarActivity extends FragmentActivity implements StatusesFragment.OnLoadingListener {
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get the loading indicator
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        super.onCreate(savedInstanceState);
    }

    private int acivityCounter = 0;
    public void incrementActivityCounter() {
        if(acivityCounter == 0 && pbLoading != null) {
            pbLoading.setVisibility(View.VISIBLE);
        }
        acivityCounter++;
    }
    public void decrementActivityCounter() {
        acivityCounter--;
        if(acivityCounter == 0 && pbLoading != null) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // add this as the on-loading listener
        if(fragment instanceof StatusesFragment) {
            StatusesFragment statusesFragment = (StatusesFragment) fragment;
            statusesFragment.setOnLoadingListener(this);
        }
    }
}

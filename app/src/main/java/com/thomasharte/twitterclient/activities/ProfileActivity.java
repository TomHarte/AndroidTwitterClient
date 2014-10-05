package com.thomasharte.twitterclient.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.fragments.UserTimelineFragment;

public class ProfileActivity extends FragmentActivity {

    public final static String EXTRA_USER_ID = "UserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();

        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();

        if(extras != null) {
            long userId = extras.getLong(EXTRA_USER_ID);
            if (userId != 0)
                userTimelineFragment.userId = userId;
        }

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, userTimelineFragment);
        transaction.commit();
    }
}

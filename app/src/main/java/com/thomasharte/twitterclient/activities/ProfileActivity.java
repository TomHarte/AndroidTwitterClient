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
import com.thomasharte.twitterclient.models.User;

public class ProfileActivity extends FragmentActivity {

    public final static String EXTRA_USER = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();

        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();

        if(extras != null) {
            User user = (User)extras.getSerializable(EXTRA_USER);
            userTimelineFragment.userId = user.getUserId();
            setTitle("@" + user.getScreenName());
        }

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, userTimelineFragment);
        transaction.commit();
    }
}

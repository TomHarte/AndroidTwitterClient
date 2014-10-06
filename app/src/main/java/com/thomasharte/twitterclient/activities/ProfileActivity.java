package com.thomasharte.twitterclient.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.fragments.UserTimelineFragment;
import com.thomasharte.twitterclient.models.User;

public class ProfileActivity extends FragmentActivity {

    public final static String EXTRA_USER = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get the user whose profile this will be
        Bundle extras = getIntent().getExtras();
        User user = (User)extras.getSerializable(EXTRA_USER);

        // set the activity title
        setTitle("@" + user.getScreenName());

        // create and configure a user timeline fragment and throw it into
        // the intended container
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        userTimelineFragment.userId = user.getUserId();

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, userTimelineFragment);
        transaction.commit();

        // populate the various other fields
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);

        TextView tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvUsername.setText(user.getScreenName());

        TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
        tvTagline.setText(user.getTagLine());

        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        tvFollowers.setText(user.getNumFollowers() + " followers");

        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        tvFollowing.setText(user.getNumFollowing() + " following");
    }
}

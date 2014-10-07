package com.thomasharte.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.TwitterApp;
import com.thomasharte.twitterclient.fragments.ComposeFragment;
import com.thomasharte.twitterclient.fragments.HomeTimelineFragment;
import com.thomasharte.twitterclient.fragments.MentionsFragment;
import com.thomasharte.twitterclient.fragments.StatusesFragment;
import com.thomasharte.twitterclient.models.Tweet;
import com.thomasharte.twitterclient.models.User;

import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;

public class TimelineAndMentionsActivity extends ProgressBarActivity implements ComposeFragment.ComposeFragmentListener {

    private FragmentTabHost tabHost;
    private HomeTimelineFragment timelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timeline_and_mentions);
        super.onCreate(savedInstanceState);

        // get the tab host
        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // add the two tabs we want, referencing the views that include
        // the appropriate fragments
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Home"), HomeTimelineFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mentions"), MentionsFragment.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_and_mentions, menu);
        return true;
    }

    public void onCompose(MenuItem menuItem) {
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.show(getFragmentManager(), "Compose Tweet");
    }

    public void onShowCurrentUser(MenuItem menuItem) {

        incrementActivityCounter();
        TwitterApp.getRestClient().verifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                handleFailureMessage(throwable, s);
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
                decrementActivityCounter();
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                decrementActivityCounter();

                User currentUser = User.fromJson(jsonObject);
                Intent intent = new Intent(TimelineAndMentionsActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPostTweet(String statusMessage) {
        incrementActivityCounter();

        TwitterApp.getRestClient().postStatus(statusMessage, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                handleFailureMessage(throwable, s);
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
                decrementActivityCounter();
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Tweet newTweet = Tweet.fromJson(jsonObject);

                if(timelineFragment != null)
                    timelineFragment.insertAtTop(newTweet);

                decrementActivityCounter();
            }
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // add this as the on-loading listener
        StatusesFragment statusesFragment = (StatusesFragment)fragment;
        statusesFragment.setOnLoadingListener(this);

        // either we now have a timeline fragment or any
        // we had has gone away
        if(fragment instanceof HomeTimelineFragment) {
            timelineFragment = (HomeTimelineFragment)fragment;
        } else {
            timelineFragment = null;
        }
    }
}

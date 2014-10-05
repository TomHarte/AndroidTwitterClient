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
import com.thomasharte.twitterclient.models.Tweet;

import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;

public class TimelineAndMentionsActivity extends FragmentActivity implements ComposeFragment.ComposeFragmentListener  {

    private FragmentTabHost tabHost;
    private ProgressBar pbLoading;
    private HomeTimelineFragment timelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_and_mentions);

        // get the tab host
        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // add the two tabs we want, referencing the views that include
        // the appropriate fragments
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Home"), HomeTimelineFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mentions"), MentionsFragment.class, null);

        // get the loading indicator (somehow?)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_and_mentions, menu);
        return true;
    }

    private int acivityCounter = 0;
    private void incrementActivityCounter() {
        if(acivityCounter == 0) {
            pbLoading.setVisibility(View.VISIBLE);
        }
        acivityCounter++;
    }
    private void decrementActivityCounter() {
        acivityCounter--;
        if(acivityCounter == 0) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

    public void onCompose(MenuItem menuItem) {
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.show(getFragmentManager(), "Compose Tweet");
    }

    public void onShowCurrentUser(MenuItem menuItem) {
        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
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

        // either we now have a timeline fragment or any
        // we had has gone away
        if(fragment instanceof HomeTimelineFragment) {
            timelineFragment = (HomeTimelineFragment)fragment;
        } else {
            timelineFragment = null;
        }
    }
}

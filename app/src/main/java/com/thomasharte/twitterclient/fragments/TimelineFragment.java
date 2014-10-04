package com.thomasharte.twitterclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thomasharte.twitterclient.TwitterClient;

/**
 * Created by thomasharte on 04/10/2014.
 */
public class TimelineFragment extends StatusesFragment {

    @Override
    protected void fetchMoreTweets() {
        fetchMoreTweets(TwitterClient.METHOD_HOME_TIMELINE);
    }

}

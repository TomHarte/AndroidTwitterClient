package com.thomasharte.twitterclient.fragments;

import com.thomasharte.twitterclient.TwitterClient;

/**
 * Created by thomasharte on 04/10/2014.
 */
public class HomeTimelineFragment extends StatusesFragment {

    @Override
    protected void fetchMoreTweets() {
        fetchMoreTweets(TwitterClient.METHOD_HOME_TIMELINE);
    }

}

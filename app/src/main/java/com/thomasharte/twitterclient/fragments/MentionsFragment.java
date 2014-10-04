package com.thomasharte.twitterclient.fragments;

import com.thomasharte.twitterclient.TwitterClient;

/**
 * Created by thomasharte on 04/10/2014.
 */
public class MentionsFragment extends StatusesFragment  {

    @Override
    protected void fetchMoreTweets() {
        fetchMoreTweets(TwitterClient.METHOD_MENTIONS_TIMELINE);
    }
}

package com.thomasharte.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.TwitterApp;
import com.thomasharte.twitterclient.TwitterClient;
import com.thomasharte.twitterclient.adaptors.TweetArrayAdaptor;
import com.thomasharte.twitterclient.models.Tweet;

import org.json.JSONArray;

import java.util.ArrayList;

public abstract class StatusesFragment extends android.support.v4.app.Fragment {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdaptor tweetsAdaptor;

    private ListView lvTweets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // establish storage for tweets
        tweets = new ArrayList<Tweet>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        // get views
        lvTweets = (ListView)view.findViewById(R.id.lvTweets);

        // establish the adaptor
        tweetsAdaptor = new TweetArrayAdaptor(container.getContext(), tweets);
        lvTweets.setAdapter(tweetsAdaptor);

        // grab the twitter client
        client = TwitterApp.getRestClient();
        lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount > totalItemCount - 4) {
                    fetchMoreTweets();
                }
            }
        });

        return view;
    }

    abstract protected void fetchMoreTweets();

    protected void fetchMoreTweets(String method) {
        fetchMoreTweets(method, null);
    }

    private boolean isFetchingTweets = false;
    protected void fetchMoreTweets(String method, Long userId) {
        if(isFetchingTweets) return;
        isFetchingTweets = true;
//        incrementActivityCounter();

        Tweet lastTweet = null;
        int numberOfTweets = tweets.size();
        if(numberOfTweets > 0)
            lastTweet = tweets.get(tweets.size() - 1);

        Long oldestId = (lastTweet != null) ? lastTweet.getTweetId()-1 : null;

        client.getStatuses(method, userId, oldestId, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                handleFailureMessage(throwable, s);
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
                endLoading();
            }

            @Override
            public void onSuccess(JSONArray jsonArray) {
                tweetsAdaptor.addAll(Tweet.fromJsonArray(jsonArray));
                endLoading();
            }

            private void endLoading() {
                isFetchingTweets = false;
//                decrementActivityCounter();
            }
        });
    }

    public void insertAtTop(Tweet tweet) {
        tweets.add(0, tweet);
        tweetsAdaptor.notifyDataSetChanged();
    }
}

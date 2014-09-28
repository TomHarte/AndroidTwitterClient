package com.thomasharte.twitterclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thomasharte.twitterclient.ComposeFragment;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.TwitterApp;
import com.thomasharte.twitterclient.TwitterClient;
import com.thomasharte.twitterclient.adaptors.TweetArrayAdaptor;
import com.thomasharte.twitterclient.models.model.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends Activity implements ComposeFragment.ComposeFragmentListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdaptor tweetsAdaptor;

    private ListView lvTweets;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get views
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.INVISIBLE);

        // establish the repository for tweets and the adaptor
        tweets = new ArrayList<Tweet>();
        tweetsAdaptor = new TweetArrayAdaptor (this, tweets);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
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

    private boolean isFetchingTweets = false;
    private void fetchMoreTweets() {
        if(isFetchingTweets) return;
        isFetchingTweets = true;
        incrementActivityCounter();

        Tweet lastTweet = null;
        int numberOfTweets = tweets.size();
        if(numberOfTweets > 0)
            lastTweet = tweets.get(tweets.size() - 1);

        Long oldestId = (lastTweet != null) ? lastTweet.getTweetId()-1 : null;

        client.getStatusesHomeTimeline(oldestId, new JsonHttpResponseHandler() {
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
                decrementActivityCounter();
            }
        });
    }

    public void onCompose(MenuItem menuItem) {
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.show(getFragmentManager(), "Search Options");
    }

    @Override
    public void onPostTweet(String statusMessage) {
        incrementActivityCounter();

        client.postStatus(statusMessage, new JsonHttpResponseHandler() {
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
                tweets.add(0, newTweet);
                tweetsAdaptor.notifyDataSetChanged();
                decrementActivityCounter();
            }
        });
    }
}

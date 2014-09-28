package com.thomasharte.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "qPe9T8l7msLy1OBASI9F7wxTm";
	public static final String REST_CONSUMER_SECRET = "xVJOTcOZpCwmbqNE2qEKm7V3et4XgCkrPSuskHcmLVWOw604Hb";
	public static final String REST_CALLBACK_URL = "oauth://codepath.com";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getStatusesHomeTimeline(Long oldestId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = null;

        if(oldestId != null) {
            params = new RequestParams();
            params.put("max_id", oldestId.toString());
        }

		client.get(apiUrl, params, handler);
    }

    public void postStatus(String statusMessage, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", statusMessage);

        client.post(apiUrl, params, handler);
    }
}
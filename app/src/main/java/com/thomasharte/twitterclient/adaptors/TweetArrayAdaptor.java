package com.thomasharte.twitterclient.adaptors;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomasharte.twitterclient.R;
import com.thomasharte.twitterclient.models.model.Tweet;
import com.thomasharte.twitterclient.models.model.User;

import java.util.ArrayList;

/**
 * Created by thomasharte on 27/09/2014.
 */
public class TweetArrayAdaptor extends ArrayAdapter<Tweet> {

    public TweetArrayAdaptor(Context context, ArrayList<Tweet> tweets) {
        super(context, 0, tweets);
    }

    private class ViewHolder {
        ImageView ivStatus, ivProfileImage;
        TextView tvStatus, tvUsername, tvScreenName, tvBody, tvPostTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // idiomatic viewholder stuff; grab the subviews and store them as
        // a tag, if we haven't already. Otherwise just grab the tag.
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.ivStatus = (ImageView)convertView.findViewById(R.id.ivStatus);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvStatus = (TextView)convertView.findViewById(R.id.tvStatusMessage);
            viewHolder.tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvPostTime = (TextView)convertView.findViewById(R.id.tvPostTime);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // features not yet implemented: tweet status (in the sense of
        // "is is a retweet?")
        viewHolder.ivStatus.setVisibility(View.GONE);
        viewHolder.tvStatus.setVisibility(View.GONE);

        // clear any persisting profile image from a previous view use
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);

        // get the tweet, the user and the image uploader
        Tweet tweet = getItem(position);
        User user = tweet.getUser();
        ImageLoader imageLoader = ImageLoader.getInstance();

        // push fields appropriately
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvUsername.setText(user.getName());
        viewHolder.tvScreenName.setText("@" + user.getScreenName());
        imageLoader.displayImage(user.getProfileImageUrl(), viewHolder.ivProfileImage);

        // figure out the relative time; default to a hard-coded 'now' if the tweet
        // appears to be in the future — that would just imply that the local clock
        // slightly disagrees with the Twitter clock
        long createdAtTime = tweet.getCreatedAt().getTime();
        long timeNow = System.currentTimeMillis();
        if(createdAtTime < timeNow) {
            viewHolder.tvPostTime.setText(DateUtils.getRelativeTimeSpanString(tweet.getCreatedAt().getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString());

        } else {
            viewHolder.tvPostTime.setText("Now");
        }

        return convertView;
    }
}

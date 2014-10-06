package com.thomasharte.twitterclient.models;

import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by thomasharte on 27/09/2014.
 */
public class User extends Model implements Serializable {

    private String name;
    private long userId;
    private String screenName;
    private String profileImageUrl;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();

        try {

            user.name = jsonObject.getString("name");
            user.userId = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");

        } catch (JSONException e) {
            return null;
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

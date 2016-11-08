package com.android.melitchi.tchat.pojos;

/**
 * Created by fonta on 08/11/2016.
 */

public class User {
    public User(String username, String url, long date) {
        this.username = username;
        this.url = url;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }
    String username;
    String url;
    public long getDate() {
        return date;
    }

    long date;

}

package com.android.melitchi.tchat.model;

/**
 * Created by fonta on 08/11/2016.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import com.android.melitchi.tchat.pojos.*;



/**
 * Created by sca on 03/06/15.
 */
public class JsonParser {

    public static List<Message> getMessages(String json) throws JSONException {
        List <Message> messages = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        Message msg;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            msg = new Message(obj.optString("username"), obj.optString("message"), obj.optLong("date"));
            messages.add(msg);
        }

        return messages;
    }

    public static List<User> getUsers(String json) throws JSONException {
        List <User> users = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        User user;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            user = new User(obj.optString("username"),obj.optString("urlPhoto"),  obj.optLong("date"));
            users.add(user);
        }

        return users;
    }

    public static String getToken(String response) throws JSONException {
        return new JSONObject(response).optString("token");
    }
}
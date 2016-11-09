package com.android.melitchi.tchat;

/**
 * Created by fonta on 08/11/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.melitchi.tchat.adapter.UsersAdapter;
import com.android.melitchi.tchat.model.HttpResult;
import com.android.melitchi.tchat.model.JsonParser;
import com.android.melitchi.tchat.model.NetworkHelper;
import com.android.melitchi.tchat.pojos.Message;
import com.android.melitchi.tchat.pojos.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UserList extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
    }
}

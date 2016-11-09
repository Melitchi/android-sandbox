package com.android.melitchi.tchat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.melitchi.tchat.adapter.MessageAdapter;
import com.android.melitchi.tchat.fragment.TchatFragment;
import com.android.melitchi.tchat.model.HttpResult;
import com.android.melitchi.tchat.model.JsonParser;
import com.android.melitchi.tchat.model.NetworkHelper;
import com.android.melitchi.tchat.pojos.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TchatActivity extends AppCompatActivity {
    private String token;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchat);
        navView=(NavigationView)findViewById(R.id.nav_menu);
        //token = Session.getInstance().getToken();
        token=PreferenceHelper.getToken(TchatActivity.this);
        Log.e("token","token value "+token);
        if (token == "") {
            Toast.makeText(this, "No token. Can't display activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TchatActivity.this,LoginActivity.class));
            finish();
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_tchat_users:
                        Intent i=new Intent(TchatActivity.this, UserList.class);
                        i.putExtra("token",token);
                        startActivity(i);
                        return(true);
                    case R.id.menu_tchat_disconnect:
                        TchatActivity.this.finish();
                        return(true);
                    case R.id.menu_tchat_refresh:
                        ((TchatFragment)getSupportFragmentManager().findFragmentById(R.id.tchat_fragment)).refresh();
                        return(true);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tchat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tchat_users:
                Intent in = new Intent(TchatActivity.this, UserList.class);
                in.putExtra("token", token);
                startActivity(in);
                return true;
        }
        return false;
    }

}

    /**
     * AsyncTask for list message
     */



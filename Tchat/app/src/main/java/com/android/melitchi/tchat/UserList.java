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
        ListView userList;
        UsersAdapter adapter;
        SwipeRefreshLayout swipeUsers;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_list);
            token = this.getIntent().getExtras().getString("token");
            if (token == null){
                Toast.makeText(this, "No token. Can't display activity", Toast.LENGTH_SHORT).show();
                finish();
            }
            adapter=new UsersAdapter(UserList.this);
            userList=(ListView)findViewById(R.id.userListView);
            swipeUsers=(SwipeRefreshLayout)findViewById(R.id.swipeUsers);
            swipeUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetUserAsyncTask(UserList.this).execute();
                    userList.setAdapter(adapter);
                }
            });
            swipeUsers.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent), this.getResources().getColor(R.color.colorPrimary));


        }


    @Override
    protected void onResume() {
        super.onResume();
        new GetUserAsyncTask(UserList.this).execute();
        userList.setAdapter(adapter);
    }

    protected class GetUserAsyncTask extends AsyncTask<String, Void, List<User>> {

        Context context;

        public GetUserAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<User> doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                return null;
            }

            InputStream inputStream = null;

            try {
                HttpResult result = NetworkHelper.doGet("http://cesi.cleverapps.io/users", null, token);
                // if ok
                if(result.code == 200) {
                    // Convert the InputStream into a string
                    return JsonParser.getUsers(result.json);
            }
                return null;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("NetworkHelper", e.getMessage());
                    }
                }
            }
        }

        @Override
        public void onPostExecute(final List<User> users){
            int nb = 0;
            if(users != null){
                nb = users.size();
                adapter.setUsers(users);
            }
            swipeUsers.setRefreshing(false);
            //Toast.makeText(messageList.this, "loaded nb messages: "+nb, Toast.LENGTH_LONG).show();

        }
    }
    }

package com.android.melitchi.tchat.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.melitchi.tchat.R;
import com.android.melitchi.tchat.Session;
import com.android.melitchi.tchat.UserList;
import com.android.melitchi.tchat.adapter.UsersAdapter;
import com.android.melitchi.tchat.model.HttpResult;
import com.android.melitchi.tchat.model.JsonParser;
import com.android.melitchi.tchat.model.NetworkHelper;
import com.android.melitchi.tchat.pojos.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fonta on 09/11/2016.
 */

public class UserFragment extends Fragment {
    private ListView userList;
    private UsersAdapter adapter;
    private SwipeRefreshLayout swipeUsers;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        adapter = new UsersAdapter(inflater.getContext());
        userList = (ListView) v.findViewById(R.id.userListView);
        swipeUsers = (SwipeRefreshLayout) v.findViewById(R.id.swipeUsers);
        swipeUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetUserAsyncTask(inflater.getContext()).execute();
                userList.setAdapter(adapter);
            }
        });
        swipeUsers.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent), this.getResources().getColor(R.color.colorPrimary));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetUserAsyncTask(UserFragment.this.getActivity()).execute();
        userList.setAdapter(adapter);
    }

    protected class GetUserAsyncTask extends AsyncTask<String, Void, List<User>> {

        Context context;

        public GetUserAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<User> doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            InputStream inputStream = null;

            try {
                HttpResult result = NetworkHelper.doGet("http://cesi.cleverapps.io/users", null, Session.getInstance().getToken());
                // if ok
                if (result.code == 200) {
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
        public void onPostExecute(final List<User> users) {
            int nb = 0;
            if (users != null) {
                nb = users.size();
                adapter.setUsers(users);
            }
            swipeUsers.setRefreshing(false);
            //Toast.makeText(TchatActivity.this, "loaded nb messages: "+nb, Toast.LENGTH_LONG).show();

        }
    }
}

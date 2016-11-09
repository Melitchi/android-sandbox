package com.android.melitchi.tchat.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.melitchi.tchat.PreferenceHelper;
import com.android.melitchi.tchat.R;
import com.android.melitchi.tchat.Session;
import com.android.melitchi.tchat.adapter.MessageAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TchatFragment extends Fragment {

    private ListView listview;
    private EditText msg;
    private Button send;
    private MessageAdapter adapter;
    private SwipeRefreshLayout swipe;
    private View view;

    private GetMessagesAsyncTask mtask;
    Timer timer;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tchat, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        msg = (EditText) v.findViewById(R.id.txtToSend);
        send = (Button) v.findViewById(R.id.sendBtn);
        swipe = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().isEmpty()) {
                    msg.setError("vous ne pouvez pas envoyer un message vide");
                    return;
                }
                new SendMessageAsyncTask().execute(msg.getText().toString());
                msg.setText("");
            }
        });
        adapter = new MessageAdapter(inflater.getContext());
        listview.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        swipe.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent), this.getResources().getColor(R.color.colorPrimary));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(task, 500, 5000);
    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        mtask.cancel(true);
    }

    public void refresh() {
        if (mtask == null || mtask.getStatus() != AsyncTask.Status.RUNNING) {
            mtask = new GetMessagesAsyncTask(TchatFragment.this.getActivity());
            mtask.execute();
        }
    }

    protected class GetMessagesAsyncTask extends AsyncTask<String, Void, List<Message>> {

        Context context;

        public GetMessagesAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<Message> doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            InputStream inputStream = null;

            try {
                HttpResult result = NetworkHelper.doGet("http://cesi.cleverapps.io/messages", null, PreferenceHelper.getToken(TchatFragment.this.getActivity()));
                // if ok
                if (result.code == 200) {
                    // Convert the InputStream into a string
                    return JsonParser.getMessages(result.json);
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
        public void onPostExecute(final List<Message> msgs) {
            int nb = 0;
            if (msgs != null) {
                nb = msgs.size();
                adapter.setMessages(msgs);

            }
            swipe.setRefreshing(false);
            //Toast.makeText(TchatActivity.this, "loaded nb messages: "+nb, Toast.LENGTH_LONG).show();

        }
    }

    protected class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;

            try {
                Map<String, String> p = new HashMap<>();
                p.put("message", params[0]);
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/messages", p, PreferenceHelper.getToken(TchatFragment.this.getActivity()));

                return result.code;

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
        public void onPostExecute(Integer status) {
            if (status != 200) {
                Toast.makeText(TchatFragment.this.getActivity(), "Error sending message", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(TchatFragment.this.getActivity(), "Message sended", Toast.LENGTH_SHORT).show();
                // Snackbar.make(view.findViewById(R.id.activity_fragment_tchat),"Message envoyé",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}

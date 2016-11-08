package com.android.melitchi.tchat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class messageList extends AppCompatActivity {
    private String token;
    private ListView listview;
    private EditText msg;
    private Button send;
    private MessageAdapter adapter;

    private GetMessagesAsyncTask mtask;
    Timer timer;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        token = this.getIntent().getExtras().getString("token");
        if (token == null){
            Toast.makeText(this, "No token. Can't display activity", Toast.LENGTH_SHORT).show();
            finish();
        }
        listview = (ListView) findViewById(R.id.listview);
        msg = (EditText) findViewById(R.id.txtToSend);
        send = (Button)findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().isEmpty()){
                    msg.setError("vous ne pouvez pas envoyer un message vide");
                    return;
                }
                new SendMessageAsyncTask().execute(msg.getText().toString());
                msg.setText("");
            }
        });
        adapter = new MessageAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tchat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_tchat_users:
                Intent in = new Intent(messageList.this, UserList.class);
                in.putExtra("token",token);
                startActivity(in);
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer=new Timer();
        timer.schedule(task,500,5000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        mtask.cancel(true);
    }

    private void refresh(){
        if (mtask == null || mtask.getStatus()!=AsyncTask.Status.RUNNING){
           mtask= new GetMessagesAsyncTask(messageList.this);
            mtask.execute();
        }

    }

    /**
     * AsyncTask for list message
     */
    protected class GetMessagesAsyncTask extends AsyncTask<String, Void, List<Message>> {

        Context context;

        public GetMessagesAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<Message> doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                return null;
            }

            InputStream inputStream = null;

            try {
                HttpResult result = NetworkHelper.doGet("http://cesi.cleverapps.io/messages", null, token);
                // if ok
                if(result.code == 200) {
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
        public void onPostExecute(final List<Message> msgs){
            int nb = 0;
            if(msgs != null){
                nb = msgs.size();
                adapter.setMessages(msgs);
            }
            //Toast.makeText(messageList.this, "loaded nb messages: "+nb, Toast.LENGTH_LONG).show();

        }
    }
    protected class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;

            try {
                Map<String, String> p = new HashMap<>();
                p.put("message", params[0]);
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/messages", p, token);

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
                Toast.makeText(messageList.this, "Error sending message", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(messageList.this, "Message sended", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

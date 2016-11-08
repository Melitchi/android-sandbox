package com.android.melitchi.tchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.melitchi.tchat.R;
import com.android.melitchi.tchat.pojos.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fonta on 08/11/2016.
 */

public class MessageAdapter extends BaseAdapter {
    private final Context context;

    public  MessageAdapter(Context ctx){this.context=ctx;}
    List<Message> messages = new LinkedList<>();


    public void setMessages(List<Message>messages){
        this.messages = messages;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(messages == null)
            return 0;
        return messages.size();
    }

    @Override
    public Message getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        view= inflater.inflate(R.layout.activity_message_item, viewGroup,false);
        TextView user =(TextView) view.findViewById(R.id.msg_user);
        TextView message =(TextView) view.findViewById(R.id.msg_message);
        TextView date =(TextView) view.findViewById(R.id.msg_date);

        user.setText(getItem(i).getUsername());
        message.setText(getItem(i).getMsg());
        DateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date netDate = (new Date(getItem(i).getDate()));

        date.setText(String.valueOf(dateForm.format(netDate)));

        return view;
    }


}

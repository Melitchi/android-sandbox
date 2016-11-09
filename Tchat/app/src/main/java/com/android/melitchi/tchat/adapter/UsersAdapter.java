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
import com.android.melitchi.tchat.pojos.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fonta on 08/11/2016.
 */

public class UsersAdapter extends BaseAdapter{
    private final Context context;

    public  UsersAdapter(Context ctx){this.context=ctx;}
    List<User> users = new LinkedList<>();

    public void setUsers(List<User>users){
        this.users = users;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(users == null)
            return 0;
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        view= inflater.inflate(R.layout.activity_item_users, viewGroup,false);
        TextView user =(TextView) view.findViewById(R.id.userlist_username);
        TextView img =(TextView) view.findViewById(R.id.userlist_img);
        TextView date=(TextView)view.findViewById(R.id.userlist_date);

        user.setText(getItem(i).getUsername());
        img.setText(getItem(i).getUrl());

        DateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date netDate = (new Date(getItem(i).getDate()));
        date.setText(String.valueOf(dateForm.format(netDate)));
        return view;
    }


}
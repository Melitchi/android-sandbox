package com.android.melitchi.tchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.melitchi.tchat.pojos.Message;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by fonta on 09/11/2016.
 */

public class MessageDao {
    private final Context context;
    private DataBaseMessageHelper dbHelper;

    public MessageDao(final Context context){
        this.context=context;
        dbHelper = new DataBaseMessageHelper(context,MessagesDB.TABLE_NAME,null,MessagesDB.DATABASE_VERSION);
    }
    public void writeMessages(List<Message> messages){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Message msg : messages){
            writeMessage(msg,db);
        }
        db.close();
    }
    private void  writeMessage(Message msg, SQLiteDatabase db){
        final ContentValues values= new ContentValues();
        values.put(MessagesDB.COLUMN_MESSAGE,msg.getMsg());
        values.put(MessagesDB.COLUMN_DATE,msg.getDate());
        values.put(MessagesDB.COLUMN_USER,msg.getUsername());
        try {
            db.insert(MessagesDB.TABLE_NAME, null, values);
        }catch (Exception e){
            Log.w("Database","le message est délà la?"+msg.getUsername()+msg.getDate(),e);
        }
    }
    public List<Message> getMessages(){
        final SQLiteDatabase db= dbHelper.getReadableDatabase();
        final String[]projection={
                MessagesDB.COLUMN_MESSAGE,
                MessagesDB.COLUMN_USER,
                MessagesDB.COLUMN_DATE
        };
        final Cursor cursor =db.query(
                MessagesDB.TABLE_NAME, //nom de la table
                projection, // projection des résultats
                null, // where clause column
                null, // where values
                null, // group by
                null, //group values
                MessagesDB.COLUMN_DATE +" DESC" //order by
                );
        final List<Message> messages = cursorToMessages(cursor);
        cursor.close();
        return messages;
    }
    private List<Message>cursorToMessages(Cursor cursor){
        final List<Message>messages = new LinkedList<>();
        while(!cursor.isLast()){
            cursor.moveToNext();
            messages.add(cursorToMessage(cursor));
        }
        return messages;
    }
    private Message cursorToMessage(Cursor cursor){
        return new Message(
                cursor.getString(cursor.getColumnIndex(MessagesDB.COLUMN_USER)),
                cursor.getString((cursor.getColumnIndex(MessagesDB.COLUMN_MESSAGE))),
                cursor.getLong(cursor.getColumnIndex(MessagesDB.COLUMN_DATE))
        );
    }
}

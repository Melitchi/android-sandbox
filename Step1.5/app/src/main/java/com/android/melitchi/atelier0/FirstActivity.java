package com.android.melitchi.atelier0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    Button bouton;
    EditText editable;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        editable=(EditText)findViewById(R.id.textToSend);
        bouton= (Button)findViewById(R.id.buttonFirstAct);
        bouton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (editable.getText().toString().isEmpty()) {
                    editable.setError("Merci de remplir le champs");
                    Toast.makeText(FirstActivity.this, "champs vide", Toast.LENGTH_SHORT).show();
                }else{
                   /* Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    intent.putExtra("sended", editable.getText().toString());
                    startActivity(intent);*/
                    displayLoader(true);
                    new HelloAsyncTask(v.getContext()).execute(editable.getText().toString());
                }
            }
        });
    }

   private void displayLoader(boolean toDisplay){
       if(toDisplay){
           progressDialog = new ProgressDialog(FirstActivity.this);
           progressDialog.setTitle("Chargement");
            progressDialog.setMessage("Envoi du hello world");
           progressDialog.show();
       }else{
           if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.cancel();
           }else{
               Toast.makeText(this, "pg inexistante", Toast.LENGTH_SHORT).show();
           }
       }
   }
    public class HelloAsyncTask extends AsyncTask<String, Void, String>{

        Context context;

        public HelloAsyncTask(final Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                return "Internet not available";
            }
            return NetworkHelper.connect(params[0]);
        }

        @Override
        protected void onPostExecute(final String s) {
            displayLoader(false);
            editable.setText(s);
        }
    }
}
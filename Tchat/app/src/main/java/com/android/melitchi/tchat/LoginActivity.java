package com.android.melitchi.tchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.melitchi.tchat.model.HttpResult;
import com.android.melitchi.tchat.model.NetworkHelper;
import com.android.melitchi.tchat.pojos.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button createAccount;
    Button connect;
    EditText login;
    EditText pass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(EditText)findViewById(R.id.login);
        pass=(EditText)findViewById(R.id.pass);
        createAccount=(Button)findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        connect=(Button)findViewById(R.id.btnConnexion);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()) {
                    //editable.setError("Merci de remplir le champs");
                    Toast.makeText(LoginActivity.this, "champs vide", Toast.LENGTH_SHORT).show();
                }else{
                    displayLoader(true);
                    new LoginActivity.HelloAsyncTask(v.getContext()).execute(login.getText().toString(),pass.getText().toString());
                }
            }
        });
    }
    private void displayLoader(boolean toDisplay){
        if(toDisplay){
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Chargement");
            progressDialog.setMessage("Connexion au compte...");
            progressDialog.show();
        }else{
            if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.cancel();
            }else{
                Toast.makeText(this, "pg inexistante", Toast.LENGTH_SHORT).show();
            }
        }
    }
public class HelloAsyncTask extends AsyncTask<String, Void,  HttpResult> {

    Context context;

    public HelloAsyncTask(final Context context){
        this.context = context;
    }

    @Override
    protected HttpResult doInBackground(String... params) {
        if(!NetworkHelper.isInternetAvailable(context)){
            return new HttpResult(500, null);
        }
        try {
            Map<String, String> theMap = new HashMap<>();
            theMap.put("username", params[0]);
            theMap.put("pwd", params[1]);
            HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/signin", theMap, null);

            return result;
        }catch (Exception e){
            Log.e("netwotkHelper",e.getMessage());
            return null;
        }
    }

    @Override
    public void onPostExecute(final HttpResult response){
        displayLoader(false);
        if(response.code == 200){
            Toast.makeText(context, "Vous êtes connecté", Toast.LENGTH_SHORT).show();
            String token="";
            try {

                token = new JSONObject(response.json).optString("token");
                Session.getInstance().setToken(token);
            } catch (JSONException e) {
                Log.e("token","unable to get token",e);
                e.printStackTrace();
            }
            if (!token.isEmpty()) {
                Intent in = new Intent(LoginActivity.this, TchatActivity.class);
                //in.putExtra("token",token);
                startActivity(in);
            }
        } else {
            Toast.makeText(LoginActivity.this, "signin failed", Toast.LENGTH_LONG).show();
        }
    }
}
}

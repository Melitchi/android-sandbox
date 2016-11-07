package com.android.melitchi.atelier0;

import android.content.Intent;
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
                    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    intent.putExtra("sended", editable.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

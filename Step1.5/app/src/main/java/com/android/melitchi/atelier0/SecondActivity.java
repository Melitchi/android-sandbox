package com.android.melitchi.atelier0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    TextView resText;
    TextView incredible;
    Button closeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent i = getIntent();
        closeBtn = (Button) findViewById(R.id.btnCloseAct);
        resText = (TextView)findViewById(R.id.result);
        resText.setText(i.getStringExtra("sended"));
        incredible=(TextView)findViewById(R.id.incredible);
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               SecondActivity.this.finish();
            }
        });
        incredible.setText(TheIncredibleQuestion.function());
    }
}

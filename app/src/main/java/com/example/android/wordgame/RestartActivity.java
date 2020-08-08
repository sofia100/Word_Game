package com.example.android.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class RestartActivity extends AppCompatActivity {
Button toss, submit;
TextView randomLetter;
EditText word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart);

        toss=findViewById(R.id.toss);
        submit=findViewById(R.id.submit);
        randomLetter=findViewById(R.id.random_letter);
        word=findViewById(R.id.word);

        toss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r=new Random();
                int n= 65 + r.nextInt(26);
                char c=(char)n;
                Log.v("toss", "letter random generated: "+c);

                randomLetter.setText(c);

            }
        });


    }
}

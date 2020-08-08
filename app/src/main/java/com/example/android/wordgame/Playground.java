package com.example.android.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Playground extends AppCompatActivity {
TextView previous_word, new_word;
EditText word;
Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        Intent intent = getIntent();
        String prev_word = intent.getStringExtra("word");


        Log.v("Word Received", "from previous activity is : "+prev_word);

        previous_word=findViewById(R.id.previous_word);
        new_word= findViewById(R.id.new_word);
        word=findViewById(R.id.word);
        submit=findViewById(R.id.submit);

        previous_word.setText(prev_word);

        //new_word retrieve from db

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String w=word.getText().toString();
                String x= new_word.getText().toString();
                int l=x.length();
                char c=x.charAt(l-1);
                if (c!= w.charAt(0))
                    Log.v("word entered", "the word" + w+" doesn't start wid the given letter "+c);

                    //else if (word not in database) then add in database

                else
                {
                    Intent i= new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);
                    startActivity(i);
                }
            }
        });





    }
}

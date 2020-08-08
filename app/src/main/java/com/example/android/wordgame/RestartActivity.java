package com.example.android.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RestartActivity extends AppCompatActivity {
Button toss, submit;
TextView randomLetter;
EditText word;
LinearLayout entry;
char c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference words = database.getReference("WORDS");


        toss=findViewById(R.id.toss);
        submit=findViewById(R.id.submit);
        randomLetter=findViewById(R.id.random_letter);
        word=findViewById(R.id.word);
        entry=findViewById(R.id.entry);

        toss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r=new Random();
                int n= 65 + r.nextInt(26);
                 c=(char)n;
                Log.v("toss", "letter random generated: "+c);

                randomLetter.setText(Character.toString(c));
                Log.v("toss", "letter random dispalyed: "+randomLetter.getText());

                toss.setVisibility(View.INVISIBLE);
                entry.setVisibility(View.VISIBLE);


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String w=word.getText().toString();

                if (c!= w.charAt(0))
                    Log.v("word entered", "the word" + w+" doesn't start wid the given letter "+c);

                //else if (word not in database) then add in database



                else
                {
                    //adding to dictionary new words
                    NewWord nw= new NewWord();
                    nw.setWord(w);
                    nw.setFlag(true);
                    nw.setKey("s00001");
                    nw.setMeaning("---");//fetch from dictionary api
                    words.setValue(nw);


                    Intent i= new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);
                    startActivity(i);
                }
            }
        });


    }
}

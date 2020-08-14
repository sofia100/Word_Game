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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        final DatabaseReference words = database.getReference("words");
        final DatabaseReference currentRef = database.getReference("current");


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

        // Read from the database and write to make all flags as false!!

        words.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot d:dataSnapshot.getChildren()
                     ) {
                    NewWord value = d.getValue(NewWord.class);

                    //value.setFlag(false);
                    words.child(value.getKey()).child("flag").setValue(false);
                    Log.d("making all flags false", "Value is: " + value.getWord());


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("making all flags false", "Failed to read value.", error.toException());
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
                    //AecRef.child().setValue(itemUpload);
                    //adding to dictionary new words
                    NewWord nw= new NewWord();
                    nw.setWord(w);
                    nw.setFlag(true);
                    String k=words.push().getKey();
                    nw.setKey(k);
                    nw.setMeaning("---");//fetch from dictionary api
                    words.child(k).setValue(nw);
                    currentRef.setValue(k);


                    Intent i= new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);
                    i.putExtra("curr_key", k);
                    startActivity(i);
                }
            }
        });


    }
}

package com.example.android.wordgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
String curr_key="--";
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

                toss.setVisibility(View.GONE);
                entry.setVisibility(View.VISIBLE);


            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String w=word.getText().toString().toUpperCase();

                if (c!= w.charAt(0)) {
                    Log.v("word entered", "the word" + w + " doesn't start wid the given letter " + c);

                    Toast.makeText(RestartActivity.this,"the word" + w + " doesn't start wid the given letter " + c , Toast.LENGTH_SHORT).show();
                    //else if (word not in database) then add in database
                }


                else
                {
                   /* //AecRef.child().setValue(itemUpload);
                    //adding to dictionary new words
                    NewWord nw= new NewWord();
                    nw.setWord(w);
                    nw.setFlag(true);
                    String k=words.push().getKey();
                    nw.setKey(k);
                    nw.setMeaning("---");//fetch from dictionary api
                    words.child(k).setValue(nw);
                    Log.v("submitted word at rstrt", "the word's flag is"+nw.getFlag());
                    currentRef.setValue(k);*/

                    final Intent i= new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);

                    words.orderByChild("word").equalTo(w)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    Log.v("words exists", "inside this");
                                                                    if (dataSnapshot.exists()) {
                                                                        //bus number exists in Database
                                                                        Log.v("words exists",w+" word exist at key "+dataSnapshot.getKey());
                                                                        for (DataSnapshot d:dataSnapshot.getChildren()
                                                                        ) {
                                                                            NewWord abc = d.getValue(NewWord.class);
                                                                            curr_key=abc.getKey();
                                                                            words.child(abc.getKey()).child("flag").setValue(true);//flag =true


                                                                            i.putExtra("curr_key", curr_key);
                                                                            currentRef.setValue(curr_key);


                                                                            Log.v("key ", "current key is "+curr_key +" for word "+w);

                                                                            Log.v("words exists",w+" word exist at key "+ abc.getKey()+" word "+abc.getWord());


                                                                        }

                                                                    } else {
                                                                        Log.v("words exists",w+ " word doesnt exist ");
                                                                        NewWord nw = new NewWord();
                                                                        nw.setWord(w.toUpperCase());
                                                                        String k=words.push().getKey();
                                                                        nw.setKey(k);
                                                                        nw.setFlag(true);
                                                                        curr_key=k;
                                                                        i.putExtra("curr_key", curr_key);
                                                                        currentRef.setValue(curr_key);

                                                                        Log.v("key ", "current key is "+curr_key +" for word "+w);
                                                                        nw.setMeaning("---");//fetch from dictionary api
                                                                        if (k==null)
                                                                        {
                                                                            Log.v("key current curr_key","key is null for w= "+w);

                                                                        }
                                                                        else{
                                                                            words.child(k).setValue(nw);
                                                                            Log.v("key current curr_key","key is not null for w= "+w +"key = "+k);

                                                                            Log.v("words exists",w+ " word added to database "+k );

                                                                        }
//                                                                        words_ref.child(k).setValue(nw);
//                                                                        Log.v("words exists",w+ " word added to database "+k );


                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.v("words exists","canceleld");


                                                                }
                                                            }
                            );



                    startActivity(i);
                }
            }
        });


    }
}

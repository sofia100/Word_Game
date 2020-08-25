package com.example.android.wordgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddWord extends AppCompatActivity {
EditText word, meaning;
Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference wordsRef = database.getReference("words");

        word = findViewById(R.id.word_add);
        meaning = findViewById(R.id.meaning_add);
        add= findViewById(R.id.add_word_button);


      /*  add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewWord w= new NewWord();
                w.setWord(word.getText().toString().trim().toUpperCase());
                w.setMeaning(meaning.getText().toString().trim().toUpperCase());
                w.setFlag(false);
                w.setKey();

            }
        });*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String w=word.getText().toString().trim().toUpperCase();


                    wordsRef.orderByChild("word").equalTo(w)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    Log.v("words exists", "inside this");
                                                                    if (dataSnapshot.exists()) {
                                                                        //bus number exists in Database
                                                                        Log.v("words exists",w+" word exist at key "+dataSnapshot.getKey());
                                                                       for (DataSnapshot d:dataSnapshot.getChildren()) {
                                                                           NewWord abc = d.getValue(NewWord.class);
                                                                            //curr_key=abc.getKey();
//                                                                            words.child(abc.getKey()).child("flag").setValue(true);//flag =true
//
//
//                                                                            i.putExtra("curr_key", curr_key);
//                                                                            currentRef.setValue(curr_key);
//
//
//                                                                            Log.v("key ", "current key is "+curr_key +" for word "+w);
//
//                                                                            Log.v("words exists",w+" word exist at key "+ abc.getKey()+" word "+abc.getWord());
//


                                                                        Toast.makeText(AddWord.this, "Word already exists in database: "+abc.getWord(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                    else {
                                                                        Log.v("words exists",w+ " word doesnt exist ");
                                                                        NewWord nw = new NewWord();
                                                                        nw.setWord(w.toUpperCase());
                                                                        String k=wordsRef.push().getKey();
                                                                        nw.setKey(k);
                                                                        nw.setFlag(false);

                                                                        nw.setMeaning(meaning.getText().toString().trim().toUpperCase());
                                                                        if (k==null)
                                                                        {
                                                                            Log.v("key current curr_key","key is null for w= "+w);

                                                                        }
                                                                        else{
                                                                            wordsRef.child(k).setValue(nw);
                                                                            Log.v("key current curr_key","key is not null for w= "+w +"key = "+k);

                                                                            Log.v("words exists",w+ " word added to database "+k );
                                                                            Toast.makeText(AddWord.this, "Word added to database: "+nw.getWord(), Toast.LENGTH_SHORT).show();


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

startActivity(new Intent(getApplicationContext(), AddWord.class));

                }
            }
        );
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
}

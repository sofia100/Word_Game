package com.example.android.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class MainActivity extends AppCompatActivity {
Button restart, resume, addword;

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference currRef = database.getReference("current");
        final DatabaseReference words = database.getReference("words");

        restart=findViewById(R.id.restart);
        resume=findViewById(R.id.resume);
        addword=findViewById(R.id.add_word);

        addword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddWord.class));
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read from the database and write to make all flags as false!!  only for once thus listner for single evnt

                words.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        for (DataSnapshot d:dataSnapshot.getChildren()
                        ) {
                            NewWord value = d.getValue(NewWord.class);
                            //setting the value of all words as flag==false when restart is pressed
                            words.child(value.getKey()).child("flag").setValue(false);
                            Log.v("making all flags false", "at main activity Value is: " + value.getWord());


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("making all flags false", "Failed to read value.", error.toException());
                    }
                });


                Intent i= new Intent(getApplicationContext(), RestartActivity.class);
                startActivity(i);
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i= new Intent(getApplicationContext(), Playground.class);
// Read from the database
                Log.v("resume :", "reached here ");

                currRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String key = dataSnapshot.getValue(String.class);
                        Log.v("resume retrieve key", "last current key is: " + key);
                        i.putExtra("curr_key", key);
                        if(!key.equals("1234567890")) {
                            DatabaseReference wordsRef = database.getReference("words/" + key);
                            Log.v("resume :", "reached here222 key "+key);

                            // Read from the database
                            wordsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    Log.v("resume :", "reached here 333 key ");

                                    for (DataSnapshot d :dataSnapshot.getChildren()
                                         ) {
                                        Log.v("resume :", "reached here 444 key ");

                                        NewWord value = dataSnapshot.getValue(NewWord.class);
                                        Log.v("word retrieve", "Value is: " + value.getWord());
                                        i.putExtra("word", value.getWord());
                                        Log.v("resume :", "reached here222 key "+value.getKey());

                                    }
                                }
// TODO: restart activity pare resume karile only "reached here" but starting ru karile playground activity open houchi but kama karuni..both crash ..most probably java xml connectn or idk yet
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.v("word retrieve", "Failed to read value.", error.toException());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.v("retrieve key", "Failed to read value.", error.toException());
                    }
                });                startActivity(i);
            }
        });
    }
}

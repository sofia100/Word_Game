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
Button restart, resume;

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

        restart=findViewById(R.id.restart);
        resume=findViewById(R.id.resume);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), RestartActivity.class);
                startActivity(i);
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i= new Intent(getApplicationContext(), Playground.class);
// Read from the database
                Log.v("resume :", "Value is: ");

                currRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String key = dataSnapshot.getValue(String.class);
                        Log.v("resume retrieve key", "Value is: " + key);
                        i.putExtra("curr_key", key);
                        if(!key.equals("1234567890")) {
                            DatabaseReference wordsRef = database.getReference("words/" + key);

                            // Read from the database
                            wordsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    NewWord value = dataSnapshot.getValue(NewWord.class);
                                    Log.v("word retrieve", "Value is: " + value.getWord());
                                    i.putExtra("word", value.getWord());
                                }

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

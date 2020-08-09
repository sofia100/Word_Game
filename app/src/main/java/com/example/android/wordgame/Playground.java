package com.example.android.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Playground extends AppCompatActivity {
TextView previous_word, new_word;
EditText word;
Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        Intent intent = getIntent();
        final String prev_word = intent.getStringExtra("word");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference words_ref = database.getReference("words");


        Log.v("Word Received", "from previous activity is : "+prev_word);

        previous_word=findViewById(R.id.previous_word);
        new_word= findViewById(R.id.new_word);
        word=findViewById(R.id.word);
        submit=findViewById(R.id.submit);

        previous_word.setText(prev_word);

        //new_word retrieve from db
        // Read from the database and write to make all flags as false!!

        words_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.



                for (DataSnapshot w:dataSnapshot.getChildren() ) {
                    NewWord value = w.getValue(NewWord.class);
                    if(prev_word.charAt(prev_word.length()-1) == value.getWord().charAt(0)) //and flag==false
                    {
                        Log.d("displaying word", "Value is: " + value.getWord());

                        new_word.setText(value.getWord());

                    }
                    Log.d("all words:", "Value is: " + value.getWord());

                }

                Log.d("showing words", "Value is:---==== " );
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

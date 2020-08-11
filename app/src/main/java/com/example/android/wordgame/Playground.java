package com.example.android.wordgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Playground extends AppCompatActivity {
TextView previous_word, new_word;
EditText word;
Button submit;
String curr_key="1234567890";
    DatabaseReference curr_ref;

void save()
{
    curr_ref.setValue(curr_key);

}
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.v("all", "on back press");
        save();
        Intent  i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        Intent intent = getIntent();
        final String prev_word = intent.getStringExtra("word");
        curr_key = intent.getStringExtra("curr_key");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference words_ref = database.getReference("words");
         curr_ref = database.getReference("current");


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
                    char c1= prev_word.charAt(prev_word.length()-1) ;
                    char c2= value.getWord().charAt(0);
                    if(c1==c2 || c1+32 ==c2 || c1-32==c2)
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
                final String w=word.getText().toString().trim();
                String x= new_word.getText().toString().trim();
                int l=x.length();
                char c1=x.charAt(l-1);
                char c2=w.charAt(0);
                if ( c1==c2 || c1+32 ==c2 || c2+32==c1 ) {

                    Intent i = new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);
                    //else if (word not in database) then add in database &&& if exists then currkey = tht key
//*************continue game not working as currentKey is unable to be updated when a new word is created.. if pushed we don't know if already in db**********
//                    words_ref.child("words").orderByChild("word").equalTo(w).once("value",snapshot => {
//                    if (snapshot.exists()){
//      const userData = snapshot.val();
//                        console.log("exists!", userData);
//                    }
//});
                    //                    words_ref.observeSingleEvent(of: .value, with: { (snapshot) in
                    //                        if snapshot.exists(){
                    //                            print("true rooms exist")
                    //                        }else{
                    //                            print("false room doesn't exist")
                    //                        }
                    //                    })

                 /*   words_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                if (data.child(w).exists()) {
                                    //do ur stuff
                                    Log.v("words exists","word exist at key "+data.getKey());
                                } else {
                                    //do something if not exists
                                    Log.v("words not exists","word doesnt exist ");

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.v("words exists","cancelled");

                        }


                    });

                 */
                    words_ref.orderByChild("word").equalTo(w)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.v("words exists", "inside this");
                                                                    if (dataSnapshot.exists()) {
                                                                        //bus number exists in Database
                                                                        Log.v("words exists",w+" word exist at key "+dataSnapshot.getKey());

                                                                    } else {
                                                                        Log.v("words exists",w+ " word doesnt exist ");

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.v("words exists","canceleld");


                                                                }
                                                            }
                            );
                    save();

                    startActivity(i);
                }


                else
                {
                    Toast.makeText(Playground.this, "the word " + w + " doesn't start wid the given letter " + c1, Toast.LENGTH_SHORT).show();
                    Log.v("word entered", "the word" + w + " doesn't start wid the given letter " + c1);
                }
            }
        });





    }
}

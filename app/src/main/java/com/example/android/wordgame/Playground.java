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

        words_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.



                for (DataSnapshot w:dataSnapshot.getChildren() ) {
                    NewWord value = w.getValue(NewWord.class);
                    char c1= prev_word.charAt(prev_word.length()-1) ;
                    char c2= value.getWord().charAt(0);
                    if((c1==c2 || c1+32 ==c2 || c1-32==c2) && !value.getFlag())
                    {
                        Log.d("displaying word", "Value is: " + value.getWord());
                        words_ref.child(value.getKey()).child("flag").setValue(true);//flag =true


                        new_word.setText(value.getWord());
                        break;

                    }
                    Log.d("all words:", "Value is: " + value.getWord());

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("displaying word from db", "Failed to read value.", error.toException());
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
                if ( c1==c2 || c1+32 ==c2 || c2+32==c1 ) {// letter matching

                    final Intent i = new Intent(getApplicationContext(), Playground.class);
                    i.putExtra("word", w);
                    //else if (word not in database) then add in database &&& if exists then currkey = tht key
//*************continue game not working as currentKey is unable to be updated when a new word is created.. if pushed we don't know if already in db**********
//***************kouthi gota null rahuchi current key for which se delete heijauchi n khali 1st time pain hin save() kamakruchi

                    words_ref.orderByChild("word").equalTo(w)//prev word exsists then use only the key for current key and flag = true
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.v("words exists", "inside this");
                                                                    if (dataSnapshot.exists()) {//prev word exists
                                                                        Log.v("words exists",w+" word exist at key "+dataSnapshot.getKey());
                                                                        for (DataSnapshot d:dataSnapshot.getChildren()
                                                                             ) {
                                                                            NewWord abc = d.getValue(NewWord.class); //retrieve word
                                                                            curr_key=abc.getKey();//current key updated as string
                                                                            Log.v("flags", abc.getWord() +"  here");

                                                                            //make true flag
                                                                            words_ref.child(abc.getKey()).child("flag").setValue(true);//flag =true
                                                                            Log.v("flags","flag for word "+ abc.getWord()+" is "+abc.getFlag());

                                                                            Log.v("flags", abc.getWord() +"  here2");

                                                                            i.putExtra("curr_key", curr_key);
                                                                            save();
                                                                            Log.v("key ", "current key is "+curr_key +" for word "+w);

                                                                            Log.v("words exists",w+" word exist at key "+ abc.getKey()+" word "+abc.getWord());


                                                                        }

                                                                    } else {
                                                                        Log.v("words exists",w+ " word doesnt exist ");
                                                                        NewWord nw = new NewWord();
                                                                        nw.setWord(w);
                                                                        nw.setFlag(true);
                                                                        String k=words_ref.push().getKey();
                                                                        nw.setKey(k);
                                                                        curr_key=k;
                                                                        i.putExtra("curr_key", curr_key);
                                                                        save();
                                                                        Log.v("key ", "current key is "+curr_key +" for word "+w);
                                                                        nw.setMeaning("---");//fetch from dictionary api
                                                                        if (k==null)
                                                                        {
                                                                            Log.v("key current curr_key","key is null for w= "+w);

                                                                        }
                                                                        else{
                                                                            words_ref.child(k).setValue(nw);
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
                    save();
                    i.putExtra("curr_key", curr_key);
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

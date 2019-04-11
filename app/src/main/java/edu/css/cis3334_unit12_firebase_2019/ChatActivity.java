package edu.css.cis3334_unit12_firebase_2019;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    Button btnPost;
    EditText etMessage;
    TextView tvMsgList;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPost = findViewById(R.id.buttonPost);
        etMessage = findViewById(R.id.editTextMessage);
        tvMsgList = findViewById(R.id.textViewMsgList);

        // Set up database reference
        myRef = FirebaseDatabase.getInstance().getReference("FireChat");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String msg = dataSnapshot.getValue(String.class);
                tvMsgList.setText("");           // clear out the all messages on the list
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    String msg = msgSnapshot.getValue(String.class);
                    tvMsgList.setText(msg+ "\n" + tvMsgList.getText());
                    //tvMsgList.append(msg);
                    //tvMsgList.append("\n");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ---- Get a new database key for the vote
                String key = myRef.push().getKey();
                // ---- set up the vote
                String msgText = etMessage.getText().toString();
                etMessage.setText("");           // clear out the all votes text box
                // ---- write the message to Firebase
                myRef.child(key).setValue(msgText);

            }
        });


    }
}

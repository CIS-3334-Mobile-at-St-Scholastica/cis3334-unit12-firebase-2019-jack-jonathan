package edu.css.cis3334_unit12_firebase_2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Set variables
    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;
    private Button buttonStartChat;
    private FirebaseAuth mAuth;

   /**
   * onCreate method to be run on creation of the program.
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        //Find variables by their ID
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonStartChat = findViewById(R.id.buttonStartChat);
        mAuth = FirebaseAuth.getInstance();

        /**
        * onClickListener for normal sign in
        * @param v
        */
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "normal login ");
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        /**
        * onClickListener for creating an account
        * @param v
        */
        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Create Account ");
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        /**
        * onClickListener for Google sign in
        * @param v
        */
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Google login ");
                googleSignIn();
            }
        });

        /**
        * onClickListener for sign out
        * @param v
        */
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Logging out - signOut ");
                signOut();
            }
        });

        /**
        * onClickListener to start the activity
        * @param v
        */
        buttonStartChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Starting Chat Intent ");
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);

            }
        });
    }

       /**
        * onStart method to check and see if the user
        * is currently signed in and updates accordingly.
        */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentUser != null) {
            // User is signed in
            Log.d("CIS3334", "onAuthStateChanged:signed_in:" + currentUser.getUid());
            Toast.makeText(MainActivity.this, "User Signed In", Toast.LENGTH_LONG).show();
            textViewStatus.setText("Signed In");
        } else {
            // User is signed out
            Log.d("CIS3334", "onAuthStateChanged:signed_out");
            Toast.makeText(MainActivity.this, "User Signed Out", Toast.LENGTH_LONG).show();
            textViewStatus.setText("Signed Out");
        }
    }

       /**
        * createAccount method that takes in an email address and password, 
        * validates them, and then creates a new user.
        * @param email
        * @param password
        */
    private void createAccount(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CIS3334", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            textViewStatus.setText("Status: Logged in!");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CIS3334", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            textViewStatus.setText("Status: Not logged in!");
                        }
                    }
                });
        //Toast.makeText(getApplicationContext(), "Create Account not implemented yet !!! ", Toast.LENGTH_LONG).show();
    }

       /**
        * signIn method that takes in an email address and password, 
        * validates them, and then signs the user in.
        * @param email
        * @param password
        */
    private void signIn(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CIS3334", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CIS3334", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        //Toast.makeText(getApplicationContext(), "signIn not implemented yet !!! ", Toast.LENGTH_LONG).show();
    }

       /**
        * signOut method that signs out the user 
        * and sets the text to "Signed Out"
        */
    private void signOut () {
        mAuth.signOut();
        textViewStatus.setText("Signed Out");
        //Toast.makeText(getApplicationContext(), "signOut not implemented yet !!! ", Toast.LENGTH_LONG).show();
    }

       /**
        * Sign in method for Google. Not implemented.
        */
    private void googleSignIn() {
        Toast.makeText(getApplicationContext(), "Google SignIn not implemented yet !!! ", Toast.LENGTH_LONG).show();
    }
}

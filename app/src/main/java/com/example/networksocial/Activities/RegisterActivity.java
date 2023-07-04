package com.example.networksocial.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networksocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;
    //Declare instance of FirebaseAuth
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Action bar and it's title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Create Account");

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setLogo();
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering");

        //In onCreate() method, init the firebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //handle register btn click
        mRegisterBtn.setOnClickListener(v -> {
            //input email, password
            String email = mEmailEt.getText().toString().trim();
            String password = mPasswordEt.getText().toString().trim();
            //Validate
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //set error and focus to email edittext
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            } else if (password.length() < 6) {
                //set error and focus to password edittext
                mPasswordEt.setError("Password length at least 6 characters");
                mPasswordEt.setFocusable(true);
            } else {
                registerUser(email, password);
            }
        });
        //Handle Login TextView click listener
        mHaveAccountTv.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }


    private void registerUser(String email, String password) {
        //email and password is valid -> show progress dialog and start registering user
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //suscess, dismiss dialog and start register activity
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //Get user email and uid
                        String userEmail = user.getEmail();
                        String userUid = user.getUid();
                        //When user registered store user into firebase realtime
                        //using HashMap
                        HashMap<Object, String> hashMap = new HashMap<>();
                        //put info into hashMap
                        hashMap.put("email",userEmail);
                        hashMap.put("uid", userUid);
                        hashMap.put("name", "");
                        hashMap.put("phone", "");
                        hashMap.put("image", "");
                        hashMap.put("cover", "");

                        FirebaseDatabase fData = FirebaseDatabase.getInstance();
                        //path to store user data named "Users"
                        DatabaseReference ref = fData.getReference("Users");
                        //put data within hashmap in database
                        ref.child(userUid).setValue(hashMap);

                        if (user != null) {
                            Toast.makeText(RegisterActivity.this, "Registered... \n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));

                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    //error, dismiss progress dialog -> get and show the message
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous activity
        return super.onSupportNavigateUp();
    }
}
package com.example.networksocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt;
    TextView mNotHaveAccountTv, mRecoveryTv;
    Button mLoginBtn;

    //Declare instance of FirebaseAuth
    private FirebaseAuth mAuth;

    //Progress Dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Login");

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setLogo();
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //
        mAuth = FirebaseAuth.getInstance();
        //Init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mNotHaveAccountTv = findViewById(R.id.nothave_accountTv);
        mRecoveryTv = findViewById(R.id.recoveryPassTv);
        mLoginBtn = findViewById(R.id.loginBtn);

        //Login button click
        mLoginBtn.setOnClickListener(v -> {
            String email = mEmailEt.getText().toString();
            String passw = mPasswordEt.getText().toString().trim();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //invalid email pattern set error
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            } else {
                //Valid email pattern
                loginUser(email, passw);
            }
        });

        //Not have account text
        mNotHaveAccountTv.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        //Recovery password
        mRecoveryTv.setOnClickListener(v -> showRecoverPasswordDialog());

        //Init progress dialog
        progressDialog = new ProgressDialog(this);
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout linear
        LinearLayout linearLayout = new LinearLayout(this);

        //views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(10);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //Recover Button
        builder.setPositiveButton("Recover", (dialog, which) -> {
            //input email
            String email = emailEt.getText().toString().trim();
            beginRecovery(email);
        });
        //Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            //Dismiss dialog
            dialog.dismiss();
        });

        //Show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        //Show progress dialog
        progressDialog.setMessage("Sending email....");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if(task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            //get and show error message
            Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    private void loginUser(String email, String passw) {
        //Show progress dialog
        progressDialog.setMessage("Logging In....");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        //Dismiss progress dialog
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //user is logged in, so start LoginActivity
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Failed!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    //Error, get and show error message
                    Toast.makeText(LoginActivity.this, "" + e.getMessage(),Toast.LENGTH_SHORT).show();

                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous activity
        return super.onSupportNavigateUp();
    }

}
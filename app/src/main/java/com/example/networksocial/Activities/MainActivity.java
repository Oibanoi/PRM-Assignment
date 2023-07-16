package com.example.networksocial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.networksocial.R;

public class MainActivity extends AppCompatActivity {

    //views
    Button registerBtn, loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.login_btn);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);


        //Handle register button
        registerBtn.setOnClickListener(v -> {
            //Start registerActivity
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));

        });

        //Handle login button
        loginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

    }
}
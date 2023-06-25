package com.example.networksocial.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.networksocial.Fragments.HomeFragment;
import com.example.networksocial.Fragments.ProfileFragment;
import com.example.networksocial.Fragments.UsersFragment;
import com.example.networksocial.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView mProfileTv;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Action bar and it's title
        ActionBar actionBar = getSupportActionBar();
        //mProfileTv = findViewById(R.id.profileTv);

        if (actionBar != null) {
            actionBar.setTitle("User Profile");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(selectedItem);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedItem = item -> {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            //home fragment
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.content ,homeFragment, "");
            ft1.commit();

            return true;
        } else if (id == R.id.nav_profile) {

            //Profile fragment
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.content ,profileFragment, "");
            ft2.commit();

            return true;
        } else if (id == R.id.nav_users) {

            //Users fragment
            UsersFragment usersFragment = new UsersFragment();
            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
            ft3.replace(R.id.content ,usersFragment, "");
            ft3.commit();
            return true;
        }
        return false;
    };


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null) {
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());
        } else {
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
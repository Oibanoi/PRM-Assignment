package com.example.networksocial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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


//    private void checkUserStatus() {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if( user != null) {
//            //user is signed in stay here
//            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
//        } else {
//            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

//    @Override
//    protected void onStart() {
//        checkUserStatus();
//        super.onStart();
//    }

    //Inflate options menu

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    //Handle menu item clicks

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //get item id
//        int id = item.getItemId();
//        if(id == R.id.action_logout) {
//            firebaseAuth.signOut();
//            checkUserStatus();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
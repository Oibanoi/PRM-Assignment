package com.example.networksocial.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.networksocial.Activities.MainActivity;
import com.example.networksocial.Adapter.AdapterUsers;
import com.example.networksocial.Models.Users;
import com.example.networksocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<Users> usersList;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        //init recycler view
        recyclerView = view.findViewById(R.id.users_recyclerView);
        //set properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init list users
        usersList = new ArrayList<>();
        //getAllUsers
        getAllUsers();
        return view;
    }

    private void getAllUsers() {
        //Get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get path of database named "Users" containing users, info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        //get All data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Users users = ds.getValue(Users.class);

                    //Get all user except currently signed in
                    if(!users.getUid().equals(firebaseUser.getUid())) {
                        usersList.add(users);
                    }
                    adapterUsers = new AdapterUsers(getActivity(), usersList);
                    //set adapter to recyclerView
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(String query) {
        //Get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get path of database named "Users" containing users, info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        //get All data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Users users = ds.getValue(Users.class);

                    //Get all searched user except currently signed in
                    if(!users.getUid().equals(firebaseUser.getUid())) {
                        if(users.getName().toLowerCase().contains(query.toLowerCase()) ||
                                users.getEmail().toLowerCase().contains(query.toLowerCase())) {
                            usersList.add(users);
                        }
                    }
                    adapterUsers = new AdapterUsers(getActivity(), usersList);
                    //refresh adapter
                    adapterUsers.notifyDataSetChanged();
                    //set adapter to recyclerView
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); //to show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    //Inflate options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        //Search View
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //Search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Call when user press search button from keyboard
            @Override
            public boolean onQueryTextSubmit(String s) {
                //If search query is not empty then search
                if(!TextUtils.isEmpty(s.trim())) {
                    searchUsers(s);
                } else {
                    //search text empty => get all users
                    getAllUsers();
                }
                return false;
            }

            //Call whenever user press any single letter from keyboard
            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())) {
                    searchUsers(s);
                } else {
                    //search text empty => get all users
                    getAllUsers();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null) {
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    //Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }


}


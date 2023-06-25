package com.example.networksocial.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
}


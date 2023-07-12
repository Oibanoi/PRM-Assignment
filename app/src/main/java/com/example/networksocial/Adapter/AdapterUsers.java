package com.example.networksocial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networksocial.Activities.ChatActivity;
import com.example.networksocial.Models.Users;
import com.example.networksocial.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<Users> userList;

    //for getting current user's id
    FirebaseAuth firebaseAuth;
    String myUid;

    public AdapterUsers(Context context, List<Users> userList) {
        this.context = context;
        this.userList = userList;
        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getUid();
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //Get data
        String userUID = userList.get(position).getUid();
        String userImage = userList.get(position).getImage();
        String userName = userList.get(position).getName();
        String userEmail = userList.get(position).getEmail();
        String userPhone = userList.get(position).getPhone();

        //Set data
        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.mAvatarTv);
        } catch (Exception e) {

        }
        holder.mPhoneTv.setText(userPhone);

        holder.mblockIv.setImageResource(R.drawable.ic_unblock);
        //check each user if blocked or not
        checkIsBlocked(userUID, holder, position);

        //Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Click user from user list to start chatting, use UID to identify */
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userUID", userUID);
                context.startActivity(intent);
            }
        });

        //Click to block, unblock user
        holder.mblockIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.get(holder.getAdapterPosition()).isBlocked()) {
                    unblockUser(userUID);
                } else {
                    blockUser(userUID);
                }
            }
        });
    }

    private void imBlockedOrNot(String userUID) {
        //first check if current user is blocked by receiver or not
        //if blocked then display a message "Cannot send message"
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(userUID).child("BlockedUsers").orderByChild("uid").equalTo(myUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            if(ds.exists()) {
                                Toast.makeText(context, "You're blocked by that user, cannot send message", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Intent intent = new Intent(context, ChatActivity.class);
                        context.startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIsBlocked(String userUID, MyHolder holder, int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(userUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            if(ds.exists()) {
                                holder.mblockIv.setImageResource(R.drawable.ic_block);
                                userList.get(position).setBlocked(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void blockUser(String userUID) {
        //block the user by adding uid to current BlockedUsers node

        //put value in hashmap
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", userUID);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").child(userUID).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //block successfully
                        Toast.makeText(context, "Block successfully!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //block fail
                        Toast.makeText(context, "Block failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unblockUser(String userUID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(userUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            if(ds.exists()) {
                                ds.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "UnBlock successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed to unblock " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //View holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarTv, mblockIv;
        TextView mNameTv, mEmailTv, mPhoneTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarTv = itemView.findViewById(R.id.avatarTv);
            mblockIv = itemView.findViewById(R.id.blockIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            mPhoneTv = itemView.findViewById(R.id.phoneTv);
        }
    }
}

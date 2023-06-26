package com.example.networksocial.Adapter;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<Users> userList;

    public AdapterUsers(Context context, List<Users> userList) {
        this.context = context;
        this.userList = userList;
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

        //Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Click user from user list to start chatting, use UID to identify */
                //Intent intent = new Intent(context, ChatActivity.class);
                //intent.putExtra("userUID", userUID);
                //context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //View holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarTv;
        TextView mNameTv, mEmailTv, mPhoneTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarTv = itemView.findViewById(R.id.avatarTv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            mPhoneTv = itemView.findViewById(R.id.phoneTv);
        }
    }
}

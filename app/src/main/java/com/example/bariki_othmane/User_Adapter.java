package com.example.bariki_othmane;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bariki_othmane.R;

import java.util.ArrayList;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.UserViewHolder> {

    ArrayList<UserHelperClass> all_users;

    public User_Adapter(ArrayList<UserHelperClass> all_users) {
        this.all_users = all_users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view_ = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_design,parent,false);
        UserViewHolder userViewHolder = new UserViewHolder(view_);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        UserHelperClass userHelperClass_ = all_users.get(position);
        holder.image.setImageResource(userHelperClass_.getImg());

    }

    @Override
    public int getItemCount() {
        return all_users.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.user_img);

        }
    }
}

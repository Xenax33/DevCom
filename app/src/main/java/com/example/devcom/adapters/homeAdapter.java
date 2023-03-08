package com.example.devcom.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devcom.BL.Person;
import com.example.devcom.BL.Post;
import com.example.devcom.R;
import com.example.devcom.databinding.PostsRvBinding;
import com.example.devcom.model.commentActivity;
import com.example.devcom.model.homeModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class homeAdapter  extends RecyclerView.Adapter<homeAdapter.viewHolder>{

    FirebaseAuth auth;
    FirebaseDatabase db;
    ArrayList<Post> list;
    Context context;
    public homeAdapter(ArrayList<Post> list, Context context) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_rv ,parent , false);
        return new viewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post model = list.get((position));
        if(model.getPostImage().isEmpty())
        {
            Log.d("image" , model.getPostImage() + "   " + model.getPostDescription());
            holder.binding.imgPostInPost.setVisibility(View.GONE);

        }
        else
        {
            holder.binding.imgPostInPost.setVisibility(View.VISIBLE);
            Picasso.get().load(model.getPostImage()).placeholder(R.drawable.logo2).into(holder.binding.imgPostInPost);

        }
        db.getReference().child("Users").child(model.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person p = snapshot.getValue(Person.class);
                holder.binding.txtName.setText(p.getName());
                Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.profile).into(holder.binding.imgProfileInPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.txtDescription.setText(model.getPostDescription());
        Log.d("postDescription" , model.getPostImage() + "   " + model.getPostDescription());
        String time = TimeAgo.using(model.getPostedAt());
        holder.binding.txtDate.setText(time);
        db.getReference().child("posts").child(model.getPostDatabaseID()).child("comment_count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                if(snapshot.exists())
                    count = Integer.parseInt(snapshot.getValue(String.class)) ;
                holder.binding.txtComment.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , commentActivity.class);
                intent.putExtra("postDBID" , model.getPostDatabaseID());
                intent.putExtra("postedBy" , model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//        holder.binding.txtDescription.setText(model.getPostDescription());
  //      holder.binding.txtDescription.setText(model.getPostDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        PostsRvBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostsRvBinding.bind(itemView);
        }
    }

}

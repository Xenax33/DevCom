package com.example.devcom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devcom.BL.Comment;
import com.example.devcom.BL.Person;
import com.example.devcom.R;
import com.example.devcom.databinding.CommentSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {
    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    Comment comment = list.get(position);
    String time = TimeAgo.using(comment.getCommentedAt());
    holder.binding.txtCommentInSample.setText(comment.getCommentContent());
    holder.binding.txtDateInSample.setText(time);

    FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Person p = snapshot.getValue(Person.class);
            holder.binding.txtNameInSample.setText(p.getName());
            Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.profile).into(holder.binding.imgProfileInSample);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CommentSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleBinding.bind(itemView);
        }
    }
}

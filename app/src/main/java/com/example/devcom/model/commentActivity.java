package com.example.devcom.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.devcom.BL.Comment;
import com.example.devcom.BL.Notification;
import com.example.devcom.BL.Post;
import com.example.devcom.R;
import com.example.devcom.adapters.CommentAdapter;
import com.example.devcom.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class commentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    String postDBID;
    String postedBy;
    Intent intent;
    FirebaseAuth auth;
    FirebaseDatabase db;
    ArrayList<Comment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        postDBID = intent.getStringExtra("postDBID");
        postedBy = intent.getStringExtra("postedBy");
        db.getReference().child("posts").child(postDBID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post p = snapshot.getValue(Post.class);
                if(!p.getPostImage().isEmpty())
                    Picasso.get().load(p.getPostImage()).placeholder(R.drawable.logo2).into(binding.imgPostInCommentActiviity);
                else
                {
                    binding.imgPostInCommentActiviity.setVisibility(View.GONE);
                }
                binding.txtDescriptionInCommentActivity.setText(p.getPostDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.imgPushComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            db.getReference().child("posts").child(postDBID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!binding.txtCommentCommentActivity.getText().toString().isEmpty())
                    {
                        Comment comment = new Comment();
                        comment.setCommentContent(binding.txtCommentCommentActivity.getText().toString());
                        comment.setCommentedBy(auth.getCurrentUser().getUid());
                        comment.setCommentedAt(new Date().getTime());
                        db.getReference().child("posts").child(postDBID).child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                db.getReference().child("posts").child(postDBID).child("comment_count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int count = 0;
                                        if(snapshot.exists())
                                        {
                                            count = Integer.parseInt(snapshot.getValue(String.class));
                                        }
                                        count = count+1;
                                        db.getReference().child("posts").child(postDBID).child("comment_count").setValue(Integer.toString(count)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(commentActivity.this, "Commented Successfully", Toast.LENGTH_SHORT).show();
                                                binding.txtCommentCommentActivity.setText("");
                                                Notification notification = new Notification();
                                                notification.setNotificationBy(auth.getUid());
                                                notification.setNotificationAt(new Date().getTime());
                                                notification.setPostID(postDBID);
                                                notification.setPostedBy(postedBy);
                                                notification.setType("comment");

                                                //saving notificatioj in DB

                                                db.getReference().child("notifications")
                                                        .child(postedBy)
                                                        .push()
                                                        .setValue(notification);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
        });
        CommentAdapter adapter  = new CommentAdapter(this , list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvComment.setLayoutManager(layoutManager);
        binding.rvComment.setAdapter(adapter);

        db.getReference().child("posts").child(postDBID).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        list.add(dataSnapshot.getValue(Comment.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
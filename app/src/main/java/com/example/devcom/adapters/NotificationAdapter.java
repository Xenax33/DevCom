package com.example.devcom.adapters;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devcom.BL.Notification;
import com.example.devcom.BL.Person;
import com.example.devcom.R;
import com.example.devcom.databinding.NotificationSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    Context context;
    ArrayList<Notification> list;

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(com.example.devcom.R.layout.notification_sample ,parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification model = list.get(position);
        holder.binding.timeNotification.setText(TimeAgo.using(model.getNotificationAt()));
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Person p = snapshot.getValue(Person.class);
                        Picasso.get().load(p.getProfile_pic()).placeholder(com.example.devcom.R.drawable.profile).into(holder.binding.imgProfileNotification);
                        if(model.getType().equals("comment"))
                        {
                            holder.binding.DescriptionNotification.setText(Html.fromHtml("<b>"+ p.getName() +"<21/b>" + " commented on your post"));
                        }
                        else
                        {
                            holder.binding.DescriptionNotification.setText(Html.fromHtml("<b>"+ p.getName() +"</b>" + " started following you"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.notificationSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getType().equals("comment"))
                {
                    FirebaseDatabase.getInstance().getReference().child("notifications")
                            .child(model.getPostedBy())
                            .child(model.getNotificationID())
                            .child("checkOpen").setValue(true);
                    Intent intent  = new Intent(context , com.example.devcom.model.commentActivity.class);
                    intent.putExtra("postDBID" , model.getPostID());
                    intent.putExtra("postedBy" , model.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                holder.binding.notificationSample.setBackgroundColor(Color.parseColor("#000000"));
            }
        });
        if(model.isCheckOpen() == true)
        {
            holder.binding.notificationSample.setBackgroundColor(Color.parseColor("#000000"));
        }
        else{}

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        NotificationSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationSampleBinding.bind(itemView);
        }
    }
}

package com.example.devcom.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcom.BL.Follower;
import com.example.devcom.BL.Notification;
import com.example.devcom.BL.Person;
import com.example.devcom.MainActivity;
import com.example.devcom.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchFragment extends Fragment {


    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase db;
    SearchView sv;
    Button follow;
    TextView id;
    TextView name;
    ImageView bgImage;
    ImageView searchViewCloseButton;
    CircleImageView profile_img;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        sv = view.findViewById(R.id.searchView);
        id = view.findViewById(R.id.id_searchFragment);
        name = view.findViewById(R.id.name_searchFragment);
        follow = view.findViewById(R.id.follow);
        bgImage = view.findViewById(R.id.bgImage);
        profile_img = view.findViewById(R.id.img_profile2);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        sv.setQueryHint("Enter ID");
        searchViewCloseButton = this.sv.findViewById(sv.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null));
        searchViewCloseButton.setColorFilter(getResources().getColor(R.color.black));
        sv.setBackgroundColor(getResources().getColor(R.color.teal_200));
        searchViewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                follow.setVisibility(View.GONE);
                profile_img.setVisibility(View.GONE);
                bgImage.setVisibility(View.VISIBLE);
                sv.setQuery("", false);
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                follow.setText("Follow");
                String parts[] = query.split("@");
                Log.d("azan", parts[0]);
                DatabaseReference ref = db.getReference().child("KeysHash").child(parts[0]);
                Task<DataSnapshot> s = ref.get();
                s.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String userID = dataSnapshot.getValue(String.class);
                            if (!userID.equals(auth.getCurrentUser().getUid())) {
                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(userID)
                                        .child("Followers").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean isFollower = false;
                                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                    Follower f = dataSnapshot1.getValue(Follower.class);
                                                    if (f.getFollowBY().equals(auth.getUid())) {
                                                        isFollower = true;
                                                        db.getReference()
                                                                .child("Users")
                                                                .child(userID)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        Person p = snapshot.getValue(Person.class);
                                                                        Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.logo).into(profile_img);
                                                                        id.setVisibility(View.VISIBLE);
                                                                        name.setVisibility(View.VISIBLE);
                                                                        follow.setVisibility(View.VISIBLE);
                                                                        profile_img.setVisibility(View.VISIBLE);
                                                                        bgImage.setVisibility(View.INVISIBLE);
                                                                        id.setText(p.getID().split("@")[0]);
                                                                        name.setText(p.getName());
                                                                        follow.setText("Followed");
                                                                        follow.setBackgroundColor(getResources().getColor(R.color.white));
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                        break;
                                                    }
                                                }
                                                if (!isFollower) {
                                                    DatabaseReference reff = db.getReference().child("Users").child(userID);
                                                    Task<DataSnapshot> ss = reff.get();
                                                    ss.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                                            Person p = dataSnapshot.getValue(Person.class);
                                                            Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.logo).into(profile_img);
                                                            id.setVisibility(View.VISIBLE);
                                                            name.setVisibility(View.VISIBLE);
                                                            follow.setVisibility(View.VISIBLE);
                                                            profile_img.setVisibility(View.VISIBLE);
                                                            bgImage.setVisibility(View.INVISIBLE);
                                                            id.setText(p.getID().split("@")[0]);
                                                            name.setText(p.getName());
                                                            Log.d("userID", "1");
                                                            follow.setText("Follow");
                                                            follow.setBackgroundColor(getResources().getColor(R.color.blue));
                                                            follow.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if (!follow.getText().toString().equals("Followed")) {
                                                                        Follower follower = new Follower();
                                                                        follower.setFollowBY(auth.getUid());
                                                                        follower.setFollowAt(new Date().getTime());
                                                                        db.getReference().child("Users")
                                                                                .child(p.getDataBaseID())
                                                                                .child("Followers")
                                                                                .child(auth.getUid())
                                                                                .setValue(follower).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        db.getReference()
                                                                                                .child("Users")
                                                                                                .child(p.getDataBaseID())
                                                                                                .child("followersCount")
                                                                                                .setValue(p.getFollowersCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        Toast.makeText(getContext(), "You Have SuccessFully Follow " + p.getName(), Toast.LENGTH_SHORT).show();
                                                                                                        follow.setText("Followed");
                                                                                                        follow.setBackgroundColor(getResources().getColor(R.color.white));
                                                                                                        // Saving NOtification in DB
                                                                                                        Notification notification = new Notification();
                                                                                                        notification.setNotificationBy(auth.getUid());
                                                                                                        notification.setNotificationAt(new Date().getTime());
                                                                                                        notification.setType("follow");
                                                                                                        db.getReference().child("notifications")
                                                                                                                .child(p.getDataBaseID())
                                                                                                                .push()
                                                                                                                .setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {

                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                });
                                                                    }
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
                        }
                    }
                });
                Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
}
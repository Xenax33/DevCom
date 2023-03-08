package com.example.devcom.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcom.BL.Person;
import com.example.devcom.Logging.Login;
import com.example.devcom.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    ImageView profile_img;
    ImageView cover;
    ImageView signout;
    TextView name;
    Button followers;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase db;
    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_img = view.findViewById(R.id.imgProfileInHome);
        cover = view.findViewById(R.id.cover);
        signout = view.findViewById(R.id.img_signout);
        name = view.findViewById(R.id.name);
        followers = view.findViewById(R.id.FriendsCount);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        db.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Person p = snapshot.getValue(Person.class);
                    Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.profile).into(profile_img);
                    Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.profile).into(cover);
                    name.setText(p.getName());
                    followers.setText(Integer.toString(p.getFollowersCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext() , Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentImg, 3);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            profile_img.setImageURI(selectedImg);
            final StorageReference reference = storage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(selectedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("Users").child(auth.getUid()).child("profile_pic").setValue(uri.toString());
                            Toast.makeText(getContext(), "Picture Has Been Uploaded", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(SignIn.this , MainActivity.class);
                            //startActivity(intent);
                            //signin.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }
}
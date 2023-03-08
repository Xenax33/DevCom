package com.example.devcom.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcom.BL.Person;
import com.example.devcom.BL.Post;
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

import java.util.Date;


public class AddFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    ImageView profile_img;
    ImageView addImg;
    ImageView img_postView;
    TextView name;
    TextView id;
    Button btn_post;
    EditText post_description;
    Uri uri;
    ProgressDialog progressDialog;
    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        name = view.findViewById(R.id.name_addFragement);
        id = view.findViewById(R.id.id_addFragment);
        post_description = view.findViewById(R.id.txt_postDescription);
        profile_img = view.findViewById(R.id.img_profile_add);
        addImg = view.findViewById(R.id.img_addImg);
        img_postView = view.findViewById(R.id.img_postView);
        img_postView.setVisibility(View.GONE);
        btn_post = view.findViewById(R.id.btn_post);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        db.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Person p = snapshot.getValue(Person.class);
                    Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.profile).into(profile_img);
                    name.setText(p.getName());
                    id.setText(p.getID());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentImg, 3);
            }
        });
        post_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String description = post_description.getText().toString();
                if (!description.isEmpty()) {
                    btn_post.setEnabled(true);
                    btn_post.setTextColor(getResources().getColor(R.color.white));
                    btn_post.setBackgroundColor(getResources().getColor(R.color.black));
                } else {
                    btn_post.setEnabled(false);
                    btn_post.setTextColor(getResources().getColor(R.color.black));
                    btn_post.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //if (!post_description.getText().toString().isEmpty()) {
                  if ( uri !=null && !Uri.EMPTY.equals((uri))) {
                        final StorageReference reference = storage.getReference().child("post_image")
                                .child(FirebaseAuth.getInstance().getUid()).child(new Date().getTime() + "");
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                    Post post = new Post();
                                    post.setPostDescription(post_description.getText().toString());
                                    post.setPostedAt(new Date().getTime());
                                    post.setPostImage(uri.toString());
                                    post.setPostedBy(auth.getCurrentUser().getUid());
                                    db.getReference()
                                            .child("posts")
                                            .push()
                                            .setValue(post)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                    }
                                });
                            }
                        });
                  }

                    else {
                      Post post = new Post();
                      post.setPostDescription(post_description.getText().toString());
                      post.setPostedAt(new Date().getTime());
                      post.setPostedBy(auth.getCurrentUser().getUid());
                      db.getReference()
                              .child("posts")
                              .push()
                              .setValue(post)
                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void unused) {
                                      progressDialog.dismiss();
                                      Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                  }
                              });
                    }
                //} else {

                //}
            }

        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            img_postView.setImageURI(uri);
            img_postView.setVisibility(View.VISIBLE);
            btn_post.setEnabled(true);
//            final StorageReference reference = storage.getReference().child("profile_pic")
//                    .child(FirebaseAuth.getInstance().getUid());
//            reference.putFile(selectedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            db.getReference().child("Users").child(auth.getUid()).child("profile_pic").setValue(uri.toString());
//                            Toast.makeText(getContext(), "Picture Has Been Uploaded", Toast.LENGTH_SHORT).show();
//                            //Intent intent = new Intent(SignIn.this , MainActivity.class);
//                            //startActivity(intent);
//                            //signin.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }
//            });
        }
    }
}
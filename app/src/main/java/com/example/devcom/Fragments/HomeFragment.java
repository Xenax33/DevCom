package com.example.devcom.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.devcom.BL.Person;
import com.example.devcom.BL.Post;
import com.example.devcom.R;
import com.example.devcom.adapters.homeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    RecyclerView rv_post;
    ArrayList<Post> postList;
    FirebaseAuth auth;
    FirebaseDatabase db;
    ImageView img_profile;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv_post = view.findViewById(R.id.rv_post);
        postList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        img_profile = view.findViewById(R.id.imgProfileInHome);
        db.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Person p = snapshot.getValue(Person.class);
                    Picasso.get().load(p.getProfile_pic()).placeholder(R.drawable.logo).into(img_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //ArrayList<Person> friends = PersonDL.getFriends(p);
//        for(int i = 0 ; i < friends.size() ; i++)
//        {
//            for(int j = 0 ; j< friends.get(i).getPostCount(); j++)
//            {
//                Post pst = friends.get(i).getPost(j);
//                postList.add(new homeModel(friends.get(i).getProfilepic() , pst.getImageID() , friends.get(i).getName() , pst.getCommentCount() , pst.getCaption(),pst.getDate()));
//            }
//        }
        // postList.add(new homeModel(R.drawable.home , R.drawable.add ,"azan" , 55 , "how to solve" , "5/5/22"));
        //postList.add(new homeModel(R.drawable.home , R.drawable.add ,"azan" , 5 , "how to solve" , "5/5/22"));
        //postList.add(new homeModel(R.drawable.home , R.drawable.add ,"azan" , 67 , "how to solve" , "5/5/22"));
        //postList.add(new homeModel(R.drawable.home , R.drawable.add ,"azan" , 45 , "how to solve" , "5/5/22"));
        // adapter
        homeAdapter adp_home = new homeAdapter(postList, getContext());
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_post.setLayoutManager(mlayoutManager);
        rv_post.setNestedScrollingEnabled(false);
        rv_post.setAdapter(adp_home);

        db.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Post p = dataSnapshot.getValue(Post.class);
                    p.setPostDatabaseID(dataSnapshot.getKey());
                    postList.add(p);
                }
                adp_home.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
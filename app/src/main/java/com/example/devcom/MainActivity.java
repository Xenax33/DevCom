package com.example.devcom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.devcom.Fragments.AddFragment;
import com.example.devcom.Fragments.HomeFragment;
import com.example.devcom.Fragments.NotificationFragment;
import com.example.devcom.Fragments.ProfileFragment;
import com.example.devcom.Fragments.SearchFragment;
import com.example.devcom.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i =  getIntent();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , new HomeFragment());
        transaction.commit();
        setContentView(binding.getRoot());
        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) { // here i is the index of bar item i.e 0 is index of home and so on...


                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(i)// we are replacing our fragment
                {
                    case 0:
                        transaction.replace(R.id.container , new HomeFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.container , new SearchFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.container , new AddFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.container , new NotificationFragment());
                        break;
                    case 4:
                        transaction.replace(R.id.container , new ProfileFragment());
                        break;
                }
                transaction.commit();//this will open the required fragment;
            }
        });

    }
}
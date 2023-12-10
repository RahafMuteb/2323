package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.googlemap.databinding.ActivityHomeBinding;
import com.example.googlemap.fragment.ProfileFragment;
import com.example.googlemap.fragment.SearchFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity  {

    ActivityHomeBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReplaceFragment(new SearchFragment());

        binding.navBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.Booking:
                        Intent intent = new Intent(HomeActivity.this , TimerActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.Home:
                        ReplaceFragment(new SearchFragment());
                        break;

                    case R.id.profile:
                        ReplaceFragment(new ProfileFragment());
                        break;

                }
                return true;
            }
        });
    }

    private void ReplaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
}
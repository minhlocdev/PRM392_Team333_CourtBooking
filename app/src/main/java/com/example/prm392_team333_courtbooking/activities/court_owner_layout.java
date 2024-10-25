package com.example.prm392_team333_courtbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.CourtListManage;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.court_manage.AddCourt;
import com.example.prm392_team333_courtbooking.court_manage.Bookings;
import com.example.prm392_team333_courtbooking.fragements.player_search.CourtFragment;
import com.example.prm392_team333_courtbooking.profile.court_owner_profile;
import com.example.prm392_team333_courtbooking.profile.player_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class court_owner_layout extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court_owner_layout);

        View appBar = findViewById(R.id.appBarLayout);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            int topInset = insets.getSystemWindowInsetTop();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = topInset;
            v.setLayoutParams(params);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, new CourtListManage());
            transaction.addToBackStack(null);
            transaction.commit();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.home) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new CourtListManage());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //selectedFragment = new CourtListManage();
                } else if (item.getItemId() == R.id.add) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new AddCourt());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else if(item.getItemId() == R.id.calendar){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new Bookings());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else if(item.getItemId() == R.id.profile){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new court_owner_profile());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //selectedFragment = new court_owner_profile();
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new CourtListManage());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                return true;
            }
        });
    }

}
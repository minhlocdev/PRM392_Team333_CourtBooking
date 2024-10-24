package com.example.prm392_team333_courtbooking.activities;

import android.annotation.SuppressLint;
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

import com.example.prm392_team333_courtbooking.CourtListManage;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.profile.player_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class court_owner_layout extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court_owner_layout);

        /*View appBar = findViewById(R.id.appBarLayout);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            int topInset = insets.getSystemWindowInsetTop();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = topInset;
            v.setLayoutParams(params);
            return insets;
        });*/

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            loadFragment(new CourtListManage());
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.add) {
                    selectedFragment = new court_owner_add();
                } else if (item.getItemId() == R.id.profile) {
                    selectedFragment = new court_owner_profile();
                }else if(item.getItemId() == R.id.calendar){
                    selectedFragment = new court_owner_calendar();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)  // R.id.main refers to the FrameLayout in your XML
                .commit();
    }
}
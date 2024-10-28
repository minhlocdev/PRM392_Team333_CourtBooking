package com.example.prm392_team333_courtbooking.activities.owner;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.Interface.TitleProvider;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.CourtListHome;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.CourtListManage;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player.player_search.CourtFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class court_owner_layout extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_owner_layout);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        // Set up the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // Set up edge-to-edge with insets
        View appBar = findViewById(R.id.appBarLayout);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = topInset;
            v.setLayoutParams(params);
            return WindowInsetsCompat.CONSUMED;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load default fragment if first launch
        if (savedInstanceState == null) {
            loadFragment(new CourtListHome(), "Home Page");
        }

        // Set up navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }


    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home){
            loadFragment(new CourtListHome(), "Home Page");
            setNavigationButtonEnabled(false);
        }
        else if (item.getItemId() == R.id.add) {
            loadFragment(new court_owner_court_manage(), "Court Management");
            setNavigationButtonEnabled(false);
        }
        else if (item.getItemId() == R.id.calendar) {
            loadFragment(new court_owner_calendar(), "Calendar");
            setNavigationButtonEnabled(false);
        }
        else if (item.getItemId() == R.id.profile) {
            loadFragment(new court_owner_profile(), "Profile");
            setNavigationButtonEnabled(false);
        }
        else return false;

        return true;
    }

    private void loadFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Update toolbar visibility based on the fragment
        if (fragment instanceof CourtListHome) {
            setToolbarVisible(false); // Hide toolbar for CourtListHome
        } else {
            setToolbarVisible(true); // Show toolbar for other fragments
            updateToolbarTitle(title);
        }
    }

    private void setToolbarVisible(boolean visible) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (visible) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }
    public void setToolbarTitle(String title) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }
    public void updateToolbarTitle(String title) {
        setToolbarTitle(title);
    }
    public void setNavigationButtonEnabled(boolean enabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setNavigationButtonEnabled(false);
            onBackPressed();  // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

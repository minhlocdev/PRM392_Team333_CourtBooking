package com.example.prm392_team333_courtbooking.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_team333_courtbooking.fragements.court_owner.login.LoginForCourtOwner;
import com.example.prm392_team333_courtbooking.R;

import SqliteHelper.Sqlite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.start_layout);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Button btnSignIn = findViewById(R.id.btn_login);
        Button btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_login){
            Intent intent = new Intent(this, LoginForCourtOwner.class);
            startActivity(intent);
        }else if(id == R.id.btn_sign_up){

            Sqlite dbHelper = new Sqlite(this);
            dbHelper.deleteDatabase(this);


            /*Intent intent = new Intent(this, SignUpForCourtOwner.class);
            startActivity(intent);*/
        }
    }
}
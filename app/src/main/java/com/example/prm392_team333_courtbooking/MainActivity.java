package com.example.prm392_team333_courtbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import SqliteHelper.Sqlite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.start_layout);


        btnSignIn = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_sign_up);

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

            /*Sqlite dbHelper = new Sqlite(this);
            dbHelper.deleteDatabase(this);*/

            Intent intent = new Intent(this, SignUpForCourtOwner.class);
            startActivity(intent);
        }
    }
}
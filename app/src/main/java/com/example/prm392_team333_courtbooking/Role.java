package com.example.prm392_team333_courtbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import Models.User;
import Repository.UserRepository;

public class Role extends AppCompatActivity implements View.OnClickListener {
    private Button btnContinue;

    private String role = "";

    private CardView cvPlayer;
    private CardView cvCourtOwner;

    private String phoneNumber;

    private UserRepository userRepository;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.role_layout);

        btnContinue = findViewById(R.id.btn_continue);
        cvPlayer = findViewById(R.id.cv_player);
        cvCourtOwner = findViewById(R.id.cv_court_owner);
        userRepository = new UserRepository(this);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        btnContinue.setOnClickListener(this);
        cvPlayer.setOnClickListener(this);
        cvCourtOwner.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_continue){
            if(role.isEmpty()){
                Toast.makeText(this, "Please choose role!", Toast.LENGTH_SHORT).show();
            }else{
                if(role.equals("Court owner")) {

                    User user = userRepository.getUserByPhoneNumber(phoneNumber);
                    user.setRole(role);
                    userRepository.updateUser(user);

                    Intent intent = new Intent(this, RegisterCourt.class);
                    intent.putExtra("user_role", role);
                    startActivity(intent);
                }else if(role.equals("Player")){

                    User user = userRepository.getUserByPhoneNumber(phoneNumber);
                    user.setRole(role);
                    userRepository.updateUser(user);

                }

            }

        }else if(id == R.id.cv_court_owner){
                role = "Court owner";
                cvCourtOwner.setCardBackgroundColor(getResources().getColor(R.color.card_background));
                cvPlayer.setCardBackgroundColor(getResources().getColor(R.color.white));
        }else if(id == R.id.cv_player){
                role = "Player";
                cvPlayer.setCardBackgroundColor(getResources().getColor(R.color.card_background));
                cvCourtOwner.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}

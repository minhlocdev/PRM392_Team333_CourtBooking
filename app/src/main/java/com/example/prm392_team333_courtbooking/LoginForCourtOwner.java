package com.example.prm392_team333_courtbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Models.CourtOwner;
import Models.User;
import Repository.CourtOwnerRepository;
import Repository.UserRepository;

public class LoginForCourtOwner extends AppCompatActivity implements View.OnClickListener{

    private EditText etPhoneNumber;
    private EditText etPassword;

    private Button btnLogin;
    private TextView txtSignUp;

    CourtOwnerRepository courtOwnerRepository;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login_for_court_owner_layout);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
        txtSignUp = findViewById(R.id.txt_sign_up);

        courtOwnerRepository = new CourtOwnerRepository(this);


        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    private void login() {
        String username = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etPhoneNumber.setError("Phone number cannot be empty");
            etPhoneNumber.requestFocus();
        } else if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
        } else {
            CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerByPhoneNumber(etPhoneNumber.getText().toString());

            if(courtOwner == null){
                etPhoneNumber.setError("No user with this phone number.");
                etPhoneNumber.requestFocus();
                return;
            }else if (!courtOwner.getPassword().equals(etPassword.getText().toString())){
                etPhoneNumber.setError("Wrong password.");
                etPhoneNumber.requestFocus();
                return;
            }

            Intent intent = new Intent(this, CourtListManage.class);
            intent.putExtra("phoneNumber", etPhoneNumber.getText().toString());
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_login){
            login();

        }else if(id == R.id.txt_sign_up){
            Intent intent = new Intent(this, SignUpForCourtOwner.class);
            startActivity(intent);
        }
    }
}

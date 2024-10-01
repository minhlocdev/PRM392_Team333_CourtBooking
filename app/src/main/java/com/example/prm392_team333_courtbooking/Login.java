package com.example.prm392_team333_courtbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Models.User;
import Repository.UserRepository;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText etPhoneNumber;
    private EditText etPassword;

    private Button btnLogin;
    private TextView txtSignUp;

    UserRepository userRepository;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login_layout);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
        txtSignUp = findViewById(R.id.txt_sign_up);

        userRepository = new UserRepository(this);


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
            User user = userRepository.getUserByPhoneNumber(etPhoneNumber.getText().toString());

            if(user == null){
                etPhoneNumber.setError("No user with this phone number.");
                etPhoneNumber.requestFocus();
                return;
            }else if (!user.getPassword().equals(etPassword.getText().toString())){
                etPhoneNumber.setError("Wrong password.");
                etPhoneNumber.requestFocus();
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_login){
            login();

        }else if(id == R.id.txt_sign_up){
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }
    }
}

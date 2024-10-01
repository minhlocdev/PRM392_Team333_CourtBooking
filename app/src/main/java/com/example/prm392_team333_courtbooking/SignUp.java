package com.example.prm392_team333_courtbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Models.User;
import Repository.UserRepository;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText etPhoneNumber;
    private EditText etPassword;

    private UserRepository userRepository;

    private TextView txtSignIn;
    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_up_layout);

        btnRegister = findViewById(R.id.btnRegister);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        txtSignIn = findViewById(R.id.tvLogin);
        userRepository = new UserRepository(this);

        btnRegister.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
    }

    private boolean validate(){
        if(etPhoneNumber.getText().toString().trim().isEmpty()){
            etPhoneNumber.setError("Phone number is required");
            etPhoneNumber.requestFocus();
            return false;
        }

        if(etPassword.getText().toString().trim().isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        User user = userRepository.getUserByPhoneNumber(etPhoneNumber.getText().toString());
        if(user != null){
            etPhoneNumber.setError("Phone number is already registered");
            etPhoneNumber.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btnRegister){
            long result = 0;
            if(validate()){
                result = userRepository.insertUser(etPassword.getText().toString(), "", "", etPhoneNumber.getText().toString(), "", LocalDateTime.now().toString(), 1);

                if(result == -1){
                    Toast.makeText(this, "Fail to create", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(this, Role.class);
                    intent.putExtra("phoneNumber", etPhoneNumber.getText().toString());
                    startActivity(intent);
                }
            }


        }else if(id == R.id.tvLogin){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}

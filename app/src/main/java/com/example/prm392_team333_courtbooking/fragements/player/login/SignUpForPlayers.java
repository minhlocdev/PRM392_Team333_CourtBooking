package com.example.prm392_team333_courtbooking.fragements.player.login;

import static Constant.SessionConstant.user;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.player.player_layout;
import java.time.LocalDateTime;
import Models.User;
import Repository.UserRepository;
import Session.SessionManager;

public class SignUpForPlayers extends AppCompatActivity implements View.OnClickListener {
    private EditText etPhoneNumber;
    private EditText etPassword;
    private EditText etConfirmedPassword;

    private UserRepository userRepository;

    private SessionManager sessionManager;

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_up_for_court_owner_layout);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        Button btnRegister = findViewById(R.id.btnRegister);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        TextView txtSignIn = findViewById(R.id.tvLogin);
        etConfirmedPassword = findViewById(R.id.etConfirmedPassword);

        sessionManager = new SessionManager(this, user);

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

        if(etConfirmedPassword.getText().toString().trim().isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if(!etConfirmedPassword.getText().toString().equals(etPassword.getText().toString())){
            etPassword.setError("Passwords are not match");
            etConfirmedPassword.setError("Passwords are not match");
            etPassword.requestFocus();
            etConfirmedPassword.requestFocus();
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
                result = userRepository.insertUser(etPassword.getText().toString(),
                        "", "",
                        etPhoneNumber.getText().toString(),
                        LocalDateTime.now().toString(), 1);

                User userPhoneNumber = userRepository.getUserByPhoneNumber(etPhoneNumber.getText().toString());

                if(result == -1){
                    Toast.makeText(this, "Fail to create", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(this, player_layout.class);
                    sessionManager.saveUserId(userPhoneNumber.getUserId());
                    startActivity(intent);
                }
            }

        }else if(id == R.id.tvLogin){
            Intent intent = new Intent(this, LoginForPlayers.class);
            startActivity(intent);
        }
    }
}

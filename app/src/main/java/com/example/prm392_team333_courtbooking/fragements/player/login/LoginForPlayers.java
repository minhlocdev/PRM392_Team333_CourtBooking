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
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.player.player_layout;
import Models.User;
import Repository.UserRepository;
import Session.SessionManager;

public class LoginForPlayers extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhoneNumber;
    private EditText etPassword;

    private SessionManager sessionManager;

    private UserRepository userRepository;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_player);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        TextView txtSignUp = findViewById(R.id.txt_sign_up);

        sessionManager = new SessionManager(this, user);

        userRepository = new UserRepository(this);

        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

        //checkSession();

    }

    private void checkSession() {
        if (sessionManager.isLoggedInUser()) {
            Intent intent = new Intent(this, player_layout.class);
            startActivity(intent);
            finish();
        }
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

            sessionManager.saveUserId(user.getUserId());

            Intent intent = new Intent(this, player_layout.class);
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
            Intent intent = new Intent(this, SignUpForPlayers.class);
            startActivity(intent);
        }
    }
}

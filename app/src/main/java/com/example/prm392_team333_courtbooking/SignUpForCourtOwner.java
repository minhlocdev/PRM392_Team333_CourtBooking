package com.example.prm392_team333_courtbooking;

import static Constant.SessionConstant.courtOwner;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_team333_courtbooking.court_manage.RegisterCourt;
import java.time.LocalDateTime;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class SignUpForCourtOwner extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhoneNumber;
    private EditText etPassword;
    private EditText etConfirmedPassword;

    private CourtOwnerRepository courtOwnerRepository;

    private SessionManager sessionManager;
    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_up_for_court_owner_layout);

        Button btnRegister = findViewById(R.id.btnRegister);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        TextView txtSignIn = findViewById(R.id.tvLogin);
        etConfirmedPassword = findViewById(R.id.etConfirmedPassword);

        sessionManager = new SessionManager(this, courtOwner);

        courtOwnerRepository = new CourtOwnerRepository(this);

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

        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerByPhoneNumber(etPhoneNumber.getText().toString());
        if(courtOwner != null){
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
            long result;
            if(validate()){
                result = courtOwnerRepository.insertCourtOwner(etPassword.getText().toString(), "", "", etPhoneNumber.getText().toString(), LocalDateTime.now().toString(), 1, "");

                CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerByPhoneNumber(etPhoneNumber.getText().toString());

                if(result == -1){
                    Toast.makeText(this, "Fail to create", Toast.LENGTH_SHORT).show();
                }else{

                    sessionManager.saveCourtOwnerId(courtOwner.getCourtOwnerId());

                    Intent intent = new Intent(this, RegisterCourt.class);
                    intent.putExtra("phoneNumber", courtOwner.getPhone());
                    startActivity(intent);
                }
            }

        }else if(id == R.id.tvLogin){
            Intent intent = new Intent(this, LoginForCourtOwner.class);
            startActivity(intent);
        }
    }
}

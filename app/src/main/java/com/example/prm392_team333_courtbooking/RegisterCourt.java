package com.example.prm392_team333_courtbooking;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import Models.User;
import Repository.UserRepository;

public class RegisterCourt extends AppCompatActivity implements View.OnClickListener{
    private EditText et_court_name;
    private EditText et_address;
    private EditText et_open_time;
    private EditText et_closed_time;

    private Button btnContinue;

    private EditText etFullName;
    private EditText etEmail;

    private UserRepository userRepository;

    private String phoneNumber;


    private int hour, minute;

    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.court_owner_information);

        et_court_name = findViewById(R.id.et_court_name);
        et_address = findViewById(R.id.et_address);
        et_open_time = findViewById(R.id.et_opening_time);
        et_closed_time = findViewById(R.id.et_closing_time);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_contact_information);
        btnContinue = findViewById(R.id.btn_continue);
        userRepository = new UserRepository(this);
        phoneNumber = getIntent().getStringExtra("phoneNumber");



        et_open_time.setOnClickListener(this);
        et_closed_time.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

    }


    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format and display the selected time in the EditText
                    timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private boolean validate() {
        // Clear previous errors
        et_court_name.setError(null);
        et_address.setError(null);
        etFullName.setError(null);
        etEmail.setError(null);

        // Validate court name
        if (et_court_name.getText().toString().trim().isEmpty()) {
            et_court_name.setError("Court name is required");
            et_court_name.requestFocus();
            return false;
        }

        // Validate address
        if (et_address.getText().toString().trim().isEmpty()) {
            et_address.setError("Address is required");
            et_address.requestFocus();
            return false;
        }

        // Validate full name
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        //Validate email
        if(etEmail.getText().toString().trim().isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.et_opening_time){
            showTimePickerDialog(et_open_time);
        }else if(id == R.id.et_closing_time){
            showTimePickerDialog(et_closed_time);
        }else if(id == R.id.btn_continue){
            if(validate()){

                User user = userRepository.getUserByPhoneNumber(phoneNumber);
                user.setFullName(etFullName.getText().toString());
                user.setEmail(etEmail.getText().toString());

                userRepository.updateUser(user);

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
            }
        }
    }
}

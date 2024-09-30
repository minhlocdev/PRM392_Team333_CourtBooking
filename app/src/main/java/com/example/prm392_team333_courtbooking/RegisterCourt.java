package com.example.prm392_team333_courtbooking;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegisterCourt extends AppCompatActivity implements View.OnClickListener{
    private EditText et_court_name;
    private EditText et_address;
    private EditText et_open_time;
    private EditText et_closed_time;

    private int hour, minute;

    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cour_owner_information);

        et_court_name = findViewById(R.id.et_court_name);
        et_address = findViewById(R.id.et_address);
        et_open_time = findViewById(R.id.et_opening_time);
        et_closed_time = findViewById(R.id.et_closing_time);

        et_open_time.setOnClickListener(this);
        et_closed_time.setOnClickListener(this);

    }


    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format and display the selected time in the EditText
                    timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);

        timePickerDialog.show();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.et_opening_time){
            showTimePickerDialog(et_open_time);
        }else if(id == R.id.et_closing_time){
            showTimePickerDialog(et_closed_time);
        }
    }
}

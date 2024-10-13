package com.example.prm392_team333_courtbooking.court_manage;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_team333_courtbooking.CourtListManage;
import com.example.prm392_team333_courtbooking.R;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;

public class RegisterCourt extends AppCompatActivity implements View.OnClickListener{
    private EditText et_court_name;
    private EditText et_address;
    private EditText et_open_time;
    private EditText et_closed_time;
    private EditText et_province;
    private LinearLayout slotContainer;
    private ImageButton btnAddSlot;
    private Button btnDone;

    private EditText etFullName;
    private EditText etEmail;

    private CourtOwnerRepository courtOwnerRepository;

    private CourtRepository courtRepository;

    private CourtSlotRepository courtSlotRepository;

    private String phoneNumber;

    private final List<View> allSlots = new ArrayList<>();

    List<LocalTime[]> slotTimes = new ArrayList<>();


    private int hour, minute;

    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_court_information);

        //get attributes
        et_court_name = findViewById(R.id.et_court_name);
        et_address = findViewById(R.id.et_address);
        et_open_time = findViewById(R.id.et_opening_time);
        et_closed_time = findViewById(R.id.et_closing_time);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_contact_information);
        et_province = findViewById(R.id.et_province);
        btnAddSlot = findViewById(R.id.btn_add_slot);
        btnDone = findViewById(R.id.btn_done);
        slotContainer = findViewById(R.id.slotContainer);

        AddSlotView();

        //intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        //initialize repo
        courtOwnerRepository = new CourtOwnerRepository(this);
        courtRepository = new CourtRepository(this);
        courtSlotRepository = new CourtSlotRepository(this);


        //set click
        et_open_time.setOnClickListener(this);
        et_closed_time.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnAddSlot.setOnClickListener(this);

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

    private boolean AddSlots() {
        // Clear previous errors
        et_court_name.setError(null);
        et_address.setError(null);
        etFullName.setError(null);
        etEmail.setError(null);


        if(!Validate(et_court_name, et_address, etFullName, etEmail, et_open_time, et_closed_time)){
            return false;
        }

        if(!ValidateTime(allSlots)){
            return false;
        }

        if(OverLap(slotTimes)){
            return false;
        }



        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerByPhoneNumber(phoneNumber);
        courtOwner.setFullName(etFullName.getText().toString());
        courtOwner.setEmail(etEmail.getText().toString());

        courtOwnerRepository.updateCourtOwner(courtOwner);

        long courtId = courtRepository.insertCourt(courtOwner.getCourtOwnerId(), et_court_name.getText().toString()
                , et_open_time.getText().toString(), et_closed_time.getText().toString()
                , et_province.getText().toString(), et_address.getText().toString(), "OPEN", null);

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            float cost;
            try{
                cost = Float.parseFloat(etCost.getText().toString());
            }catch (Exception e){
                etCost.setError("Invalid cost format (00.00)");
                return false;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            courtSlotRepository.insertCourtSlot( (int)courtId, timeStart, timeEnd, cost);
        }


        return true;
    }

    private boolean Validate(EditText et_court_name, EditText et_address, EditText etFullName, EditText etEmail, EditText et_open_time, EditText et_closed_time){

        // Validate court name
        if (et_court_name.getText().toString().trim().isEmpty() || et_court_name.getText().toString().isBlank()) {
            et_court_name.setError("Court name is required");
            et_court_name.requestFocus();
            return false;
        }

        // Validate address
        if (et_address.getText().toString().trim().isEmpty() || et_address.getText().toString().isBlank()) {
            et_address.setError("Address is required");
            et_address.requestFocus();
            return false;
        }

        // Validate province
        if (et_province.getText().toString().trim().isEmpty() || et_province.getText().toString().isBlank()) {
            et_province.setError("Province is required");
            et_province.requestFocus();
            return false;
        }

        // Validate full name
        if (etFullName.getText().toString().trim().isEmpty() || et_province.getText().toString().isBlank()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        //Validate email
        if(etEmail.getText().toString().trim().isEmpty() || etEmail.getText().toString().isBlank()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if(et_open_time.getText().toString().trim().isEmpty() || et_open_time.getText().toString().isBlank()){
            et_open_time.setError("Open time is required");
            et_open_time.requestFocus();
            return false;
        }

        if(et_closed_time.getText().toString().trim().isEmpty() || et_closed_time.getText().toString().isBlank()){
            et_closed_time.setError("Closed time is required");
            et_closed_time.requestFocus();
            return false;
        }

        try{
            if(!LocalTime.parse(et_open_time.getText().toString()).isBefore(LocalTime.parse(et_closed_time.getText().toString()))){
                Toast.makeText(this, "Open time is bigger than closed time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            et_open_time.setError("Invalid time format (HH:mm)");
            et_closed_time.setError("Invalid time format (HH:mm)");
            return false;
        }

        return true;
    }

    private boolean ValidateTime(List<View> allSlots){
        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();
            String cost = etCost.getText().toString();

            if(timeStart.isEmpty() || timeStart.isBlank()){
                etTimeStart.setError("Time start cannot be empty");
                return false;
            }

            if (timeEnd.isEmpty()) {
                etTimeEnd.setError("Time end cannot be empty");
                return false;
            }
            if (cost.isEmpty()) {
                etCost.setError("Cost cannot be empty");
                return false;
            }

            try {
                LocalTime timeStarT = LocalTime.parse(timeStart);
                LocalTime timeEndT = LocalTime.parse(timeEnd);
                if (!timeStarT.isBefore(timeEndT)) {
                    etTimeStart.setError("Time start must be earlier than time end");
                    etTimeEnd.setError("Time end must be later than time start");
                    return false;
                }

                // Store start and end times for overlap checking
                slotTimes.add(new LocalTime[]{timeStarT, timeEndT});
            }
            catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
            }
        }
        return true;
    }

    private boolean OverLap( List<LocalTime[]> slotTimes){
        for (int i = 0; i < slotTimes.size(); i++) {
            LocalTime[] currentSlot = slotTimes.get(i);
            LocalTime currentStart = currentSlot[0];
            LocalTime currentEnd = currentSlot[1];

            for (int j = i + 1; j < slotTimes.size(); j++) {
                LocalTime[] nextSlot = slotTimes.get(j);
                LocalTime nextStart = nextSlot[0];
                LocalTime nextEnd = nextSlot[1];

                // Check if current slot overlaps with the next one
                if (currentStart.isBefore(nextEnd) && nextStart.isBefore(currentEnd)) {
                    Toast.makeText(this, "Slot " + (i + 1) + " overlaps with Slot " + (j + 1), Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }

        return false;
    }

    private void AddSlotView(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        EditText etTimeStart = newSlot.findViewById(R.id.et_time_start);
        EditText etTimeEnd = newSlot.findViewById(R.id.et_time_end);

        etTimeStart.setOnClickListener( v -> {
            showTimePickerDialog(etTimeStart);
        });

        etTimeEnd.setOnClickListener( v -> {
            showTimePickerDialog(etTimeEnd);
        });

        int index = slotContainer.indexOfChild(findViewById(R.id.btn_add_slot));

        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.et_opening_time){
            showTimePickerDialog(et_open_time);
        }
        else if(id == R.id.et_closing_time){
            showTimePickerDialog(et_closed_time);
        }
        else if(id == R.id.btn_done){
            if(!AddSlots()){
                Toast.makeText(this, "Fail to create your court", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this, CourtListManage.class);
            intent.putExtra("phoneNumber", phoneNumber);
            startActivity(intent);

            }
        else if(id == R.id.btn_add_slot){
            AddSlotView();
        }
        else{
            Toast.makeText(this, "Fail validation", Toast.LENGTH_SHORT).show();
        }
    }
}


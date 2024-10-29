package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import static Constant.SessionConstant.courtOwner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_court_manage;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Court;
import Models.CourtSlot;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;
import Session.SessionManager;

public class EditCourt extends AppCompatActivity implements View.OnClickListener {

    private EditText etCourtName;
    private EditText etAddress;
    private EditText etOpenTime;
    private EditText etClosedTime;
    private EditText etProvince;
    private Spinner spinnerStatus ;
    private LinearLayout slotContainer;

    private int courtId;

    private final List<View> allSlots = new ArrayList<>();

    List<LocalTime[]> slotTimes = new ArrayList<>();


    private final CourtRepository courtRepository = new CourtRepository(this);

    private final CourtSlotRepository courtSlotRepository = new CourtSlotRepository(this);

    private int hour, minute;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_court_layout);
        View appBar = findViewById(R.id.editAppbar);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            int topInset = insets.getSystemWindowInsetTop();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = topInset+10;
            v.setLayoutParams(params);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Handle back button press
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etCourtName = findViewById(R.id.et_court_name);
        etAddress = findViewById(R.id.et_address);
        etOpenTime = findViewById(R.id.et_opening_time);
        etClosedTime = findViewById(R.id.et_closing_time);
        etProvince = findViewById(R.id.et_province);
        spinnerStatus  = findViewById(R.id.spinner_court_status);
        slotContainer = findViewById(R.id.slotContainer);
        ImageButton ibAddSlotView = findViewById(R.id.btn_add_slot);
        Button btnSave = findViewById(R.id.btn_save);

        List<String> statusList = new ArrayList<>();

        Intent intent = getIntent();

        courtId = intent.getIntExtra("court_id", 0);

        Court court = courtRepository.getCourtById(courtId);

        if(court == null){

        }else{
            etCourtName.setText(court.getCourtName());
            etAddress.setText(court.getAddress());
            etOpenTime.setText(court.getOpenTime());
            etClosedTime.setText(court.getClosedTime());
            etProvince.setText(court.getProvince());

            if(court.getStatus().equals("OPEN")){
                statusList.add("OPEN");
                statusList.add("CLOSED");
                spinnerStatus.setBackgroundColor(Color.GREEN);
            }else{
                statusList.add("OPEN");
                spinnerStatus.setBackgroundColor(Color.RED);
            }
        }

        List<CourtSlot> existingSlots = courtSlotRepository.getSlotsByCourtId(courtId);

        for (CourtSlot slot : existingSlots) {
            LoadSlotView(slot.getCourtSlotId(), slot.getTimeStart(), slot.getTimeEnd(), slot.getCost());
        }


        ArrayAdapter<String> adapter = getAdapter(statusList);

        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = statusList.get(position);
                if (selectedStatus.equals("OPEN")) {
                    spinnerStatus.setBackgroundColor(Color.GREEN);
                } else if (selectedStatus.equals("CLOSED")) {
                    spinnerStatus.setBackgroundColor(Color.RED);
                }

                assert court != null;
                court.setStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Another callback interface that is invoked when no item is selected
            }
        });

        ibAddSlotView.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private @NonNull ArrayAdapter<String> getAdapter(List<String> statusList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList){
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                // Set background color based on the item (OPEN or CLOSED)
                if (statusList.get(position).equals("OPEN")) {
                    textView.setBackgroundColor(Color.GREEN);
                } else if (statusList.get(position).equals("CLOSED")) {
                    textView.setBackgroundColor(Color.RED);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_court_status_item);
        return adapter;
    }

   /* private boolean AddSlots() {
        // Validate basic inputs first
        if (!ValidateInputs()) {
            return false;
        }

        // Ensure each slot's time meets the format, duration, and open/close hours
        if (!ValidateTime(allSlots)) {
            return false;
        }

        // Check for any overlaps in slot times
        if (hasOverlappingSlots(slotTimes)) {
            return false;
        }

        // Parse court's open and close times
        LocalTime openTime;
        LocalTime closeTime;
        try {
            openTime = LocalTime.parse(etOpenTime.getText().toString());
            closeTime = LocalTime.parse(etClosedTime.getText().toString());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid open or close time format", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            float cost;
            try {
                cost = Float.parseFloat(etCost.getText().toString());
                if (cost < 0) {
                    etCost.setError("Cost cannot be less than 0");
                    return false;
                }
            } catch (Exception e) {
                etCost.setError("Invalid cost format (00.00)");
                return false;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            // Parse slot times
            LocalTime slotStart;
            LocalTime slotEnd;
            try {
                slotStart = LocalTime.parse(timeStart);
                slotEnd = LocalTime.parse(timeEnd);
            } catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
            }

            // Check if the slot time is within open and close time
            if (slotStart.isBefore(openTime) || slotEnd.isAfter(closeTime)) {
                Toast.makeText(this, "Slot times must be within open and close times", Toast.LENGTH_LONG).show();
                return false;
            }

            // Update or insert the slot as appropriate
            int courtSlotId = Integer.parseInt(slot.getTag().toString());
            CourtSlot courtSlot = courtSlotRepository.getSlotById(courtSlotId);

            //courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);

            if(courtSlot != null){
                courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);
            } else {
                courtSlotRepository.insertCourtSlot(courtId, timeStart, timeEnd, cost);
            }
        }
        // Proceed to add slots if all validations are successful
        *//*for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            try {
                float cost = Float.parseFloat(etCost.getText().toString());
                LocalTime timeStart = LocalTime.parse(etTimeStart.getText().toString());
                LocalTime timeEnd = LocalTime.parse(etTimeEnd.getText().toString());

                int courtSlotId = Integer.parseInt(slot.getTag().toString());
                CourtSlot courtSlot = courtSlotRepository.getSlotById(courtSlotId);
                courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart.toString(), timeEnd.toString(), cost);

                if(courtSlot != null){
                    courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart.toString(), timeEnd.toString(), cost);
                } else {
                    courtSlotRepository.insertCourtSlot(courtId, timeStart.toString(), timeEnd.toString(), cost);
                }

            } catch (Exception e) {
                Toast.makeText(this, "Error saving slot information.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }*//*
        return true;
    }*/

    private boolean AddSlots() {
        // Validate basic inputs first
        if (!ValidateInputs()) {
            return false;
        }

        // Ensure each slot's time meets the format, duration, and open/close hours
        if (!ValidateTime(allSlots)) {
            return false;
        }

        // Check for any overlaps in slot times
        if (hasOverlappingSlots(slotTimes)) {
            return false;
        }

        // Proceed with saving the slots if all validations pass
        SessionManager sessionManager = new SessionManager(this, courtOwner);
        courtRepository.updateCourt(
                courtId,
                etCourtName.getText().toString(),
                etOpenTime.getText().toString(),
                etClosedTime.getText().toString(),
                etProvince.getText().toString(),
                etAddress.getText().toString()
        );
        Court court = courtRepository.getCourtById(courtId);

        // Insert each slot into the database
        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            float cost;
            try {
                cost = Float.parseFloat(etCost.getText().toString());
                if (cost < 0) {
                    etCost.setError("Cost cannot be less than 0");
                    return false;
                }
            } catch (Exception e) {
                etCost.setError("Invalid cost format (00.00)");
                return false;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            // Update or insert the slot as appropriate
            int courtSlotId = Integer.parseInt(slot.getTag().toString());
            CourtSlot courtSlot = courtSlotRepository.getSlotById(courtSlotId);

            //courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);

            if(courtSlot != null){
                courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);
            } else {
                courtSlotRepository.insertCourtSlot(courtId, timeStart, timeEnd, cost);
            }
        }
        return true;
    }

    private boolean hasOverlappingSlots(List<LocalTime[]> slotTimes) {
        for (int i = 0; i < slotTimes.size(); i++) {
            LocalTime[] currentSlot = slotTimes.get(i);
            for (int j = i + 1; j < slotTimes.size(); j++) {
                LocalTime[] otherSlot = slotTimes.get(j);
                if (currentSlot[0].isBefore(otherSlot[1]) && otherSlot[0].isBefore(currentSlot[1])) {
                    Toast.makeText(this, "Slot " + (i + 1) + " overlaps with Slot " + (j + 1), Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    private void AddSlotView(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        int index = slotContainer.indexOfChild(view.findViewById(R.id.btn_add_slot));

        newSlot.setTag(0);
        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    // In ValidateInputs(), check if the court's open and close times meet the requirements
    private boolean ValidateInputs() {
        // Basic validation for input fields (name, address, open/close times, province)
        if (etCourtName.getText().toString().isEmpty() || etAddress.getText().toString().isEmpty()
                || etProvince.getText().toString().isEmpty() || etOpenTime.getText().toString().isEmpty()
                || etClosedTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            LocalTime openTime = LocalTime.parse(etOpenTime.getText().toString());
            LocalTime closeTime = LocalTime.parse(etClosedTime.getText().toString());

            // Check if open and close times meet hour or half-hour increments and are at least 90 minutes apart
            if (!isValidMinute(openTime) || !isValidMinute(closeTime)) {
                Toast.makeText(this, "Open and Close times must be on the hour or half-hour.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (java.time.Duration.between(openTime, closeTime).toMinutes() < 90) {
                Toast.makeText(this, "Court open and close times must span at least 90 minutes.", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid time format. Use HH:mm.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // In ValidateTime(), check if each slot meets the 90-minute duration requirement
    private boolean ValidateTime(List<View> allSlots) {
        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            try {
                LocalTime timeStartT = LocalTime.parse(timeStart);
                LocalTime timeEndT = LocalTime.parse(timeEnd);

                // Ensure slot start and end times meet hour or half-hour increments
                if (!isValidMinute(timeStartT) || !isValidMinute(timeEndT)) {
                    etTimeStart.setError("Time start and end must be on the hour or half-hour.");
                    etTimeEnd.setError("Time start and end must be on the hour or half-hour.");
                    return false;
                }

                /*// Ensure each slot duration is at least 90 minutes
                if (java.time.Duration.between(timeStartT, timeEndT).toMinutes() < 90) {
                    etTimeStart.setError("Each slot must be at least 90 minutes long.");
                    etTimeEnd.setError("Each slot must be at least 90 minutes long.");
                    return false;
                }*/

                // Ensure each slot is within open and close hours
                if (!isSlotWithinCourtHours(timeStartT, timeEndT)) {
                    etTimeStart.setError("Slot times must be within court open and close hours.");
                    etTimeEnd.setError("Slot times must be within court open and close hours.");
                    return false;
                }

                // Store start and end times for overlap checking
                slotTimes.add(new LocalTime[]{timeStartT, timeEndT});
            } catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
            }
        }
        return true;
    }

    private void showTimePickerDialog(final EditText timeField) {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker view, int selectedHour, int selectedMinute) -> {
            if (selectedMinute == 0 || selectedMinute == 30) {
                timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            } else {
                Toast.makeText(this, "Please select a time with minutes as 00 or 30 only.", Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private boolean isValidMinute(LocalTime time) {
        int minute = time.getMinute();
        return minute == 0 || minute == 30;
    }

    private boolean isSlotWithinCourtHours(LocalTime slotStart, LocalTime slotEnd) {
        try {
            LocalTime openTime = LocalTime.parse(etOpenTime.getText().toString());
            LocalTime closeTime = LocalTime.parse(etClosedTime.getText().toString());

            // Check if the slot start and end times are within the open and close times
            if (slotStart.isBefore(openTime) || slotEnd.isAfter(closeTime)) {
                Toast.makeText(this, "Slot times must be within the court's open and close hours.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid open or close time format.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidSlotDuration(LocalTime startTime, LocalTime endTime) {
        return java.time.Duration.between(startTime, endTime).toMinutes() >= 90;
    }

    private void LoadSlotView(int slotId, String startTime, String endTime, double cost) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        // Find the input fields inside the slot layout
        EditText etTimeStart = newSlot.findViewById(R.id.et_time_start);
        EditText etTimeEnd = newSlot.findViewById(R.id.et_time_end);
        EditText etCost = newSlot.findViewById(R.id.et_cost);

        // Set the slot values
        etTimeStart.setText(startTime);
        etTimeEnd.setText(endTime);
        etCost.setText(String.valueOf(cost));

        etTimeStart.setOnClickListener( v -> {
            showTimePickerDialog(etTimeStart);
        });

        etTimeEnd.setOnClickListener( v -> {
            showTimePickerDialog(etTimeEnd);
        });

        // Save the slotId in the tag of the view for tracking purposes
        newSlot.setTag(slotId);

        int index = slotContainer.indexOfChild(findViewById(R.id.btn_add_slot));

        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    private void setError(String message, EditText... fields) {
        for (EditText field : fields) {
            field.setError(message);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.et_opening_time){
            showTimePickerDialog(etOpenTime);
        }else if(id== R.id.et_closing_time){
            showTimePickerDialog(etClosedTime);
        }else if(id == R.id.btn_add_slot){
            AddSlotView(view);
        }else if(id == R.id.btn_save){
            if(AddSlots()){
                Toast.makeText(this, "Court Update Successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }
}

package com.example.prm392_team333_courtbooking.court_manage;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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

import com.example.prm392_team333_courtbooking.R;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Court;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;

public class AddCourt extends AppCompatActivity implements View.OnClickListener {

    private EditText etCourtName;
    private EditText etAddress;
    private EditText etOpenTime;
    private EditText etClosedTime;
    private EditText etProvince;
    private Spinner spinnerStatus;
    private LinearLayout slotContainer;

    ImageButton ibAddSlotView = findViewById(R.id.btn_add_slot);
    Button btnSave = findViewById(R.id.btn_save);
    private final List<View> allSlots = new ArrayList<>();
    List<LocalTime[]> slotTimes = new ArrayList<>();

    private final CourtRepository courtRepository = new CourtRepository(this);
    private final CourtSlotRepository courtSlotRepository = new CourtSlotRepository(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_court_layout);  // Ensure you have a layout for adding a court
        View appBar = findViewById(R.id.addAppbar);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            int topInset = insets.getSystemWindowInsetTop();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = topInset + 10;
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
        spinnerStatus = findViewById(R.id.spinner_court_status);
        slotContainer = findViewById(R.id.slotContainer);
        ImageButton ibAddSlotView = findViewById(R.id.btn_add_slot);
        Button btnSave = findViewById(R.id.btn_save);

        // Populate status options
        List<String> statusList = new ArrayList<>();
        statusList.add("OPEN");
        statusList.add("CLOSED");

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Another callback interface
            }
        });

        ibAddSlotView.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private @NonNull ArrayAdapter<String> getAdapter(List<String> statusList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

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

    private boolean OverLap(List<LocalTime[]> slotTimes) {
        for (int i = 0; i < slotTimes.size(); i++) {
            LocalTime[] currentSlot = slotTimes.get(i);
            LocalTime currentStart = currentSlot[0];
            LocalTime currentEnd = currentSlot[1];

            for (int j = i + 1; j < slotTimes.size(); j++) {
                LocalTime[] nextSlot = slotTimes.get(j);
                LocalTime nextStart = nextSlot[0];
                LocalTime nextEnd = nextSlot[1];

                if (currentStart.isBefore(nextEnd) && nextStart.isBefore(currentEnd)) {
                    Toast.makeText(this, "Slot " + (i + 1) + " overlaps with Slot " + (j + 1), Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    private void AddSlots() {
        if (!Validate(etCourtName, etAddress, etOpenTime, etClosedTime, etProvince)) {
            return;
        }

        if (!ValidateTime(allSlots)) {
            return;
        }

        if (OverLap(slotTimes)) {
            return;
        }

        // Insert new court data
        Court court = new Court(
                Integer.parseInt(),
                etCourtName.getText().toString(),
                etAddress.getText().toString(),
                etOpenTime.getText().toString(),
                etClosedTime.getText().toString(),
                etProvince.getText().toString(),
                spinnerStatus.getSelectedItem().toString()
        );
        int courtId = courtRepository.insertCourt(
                court.getCourtOwnerId(),
                court.getCourtName(),
                court.getOpenTime(),
                court.getClosedTime(),
                court.getProvince(),
                court.getAddress(),
                court.getStatus(),
                court.getImage());

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            float cost;
            try {
                cost = Float.parseFloat(etCost.getText().toString());
            } catch (Exception e) {
                etCost.setError("Invalid cost format (00.00)");
                return;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            courtSlotRepository.insertCourtSlot(courtId, timeStart, timeEnd, cost);
        }
    }

    private boolean Validate(EditText etCourtName, EditText etAddress, EditText etOpenTime, EditText etClosedTime, EditText etProvince) {
        // Validation logic
        if (etCourtName.getText().toString().trim().isEmpty()) {
            etCourtName.setError("Court name is required");
            etCourtName.requestFocus();
            return false;
        }

        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }

        if (etProvince.getText().toString().trim().isEmpty()) {
            etProvince.setError("Province is required");
            etProvince.requestFocus();
            return false;
        }

        if (etOpenTime.getText().toString().trim().isEmpty()) {
            etOpenTime.setError("Open time is required");
            etOpenTime.requestFocus();
            return false;
        }

        if (etClosedTime.getText().toString().trim().isEmpty()) {
            etClosedTime.setError("Closed time is required");
            etClosedTime.requestFocus();
            return false;
        }

        try {
            if (!LocalTime.parse(etOpenTime.getText().toString()).isBefore(LocalTime.parse(etClosedTime.getText().toString()))) {
                Toast.makeText(this, "Open time is later than closed time", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (DateTimeParseException e) {
            etOpenTime.setError("Invalid time format (HH:mm)");
            etClosedTime.setError("Invalid time format (HH:mm)");
            return false;
        }

        return true;
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

    private void AddSlotView(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        int index = slotContainer.indexOfChild(findViewById(R.id.btn_add_slot));

        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format and display the selected time in the EditText
                    timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private boolean ValidateTime(List<View> allSlots) {
        for (int i = 0; i < allSlots.size(); i++) {
            View slot = allSlots.get(i);
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);

            try {
                LocalTime startTime = LocalTime.parse(etTimeStart.getText().toString());
                LocalTime endTime = LocalTime.parse(etTimeEnd.getText().toString());

                slotTimes.add(new LocalTime[]{startTime, endTime});

                if (!startTime.isBefore(endTime)) {
                    Toast.makeText(this, "Slot " + (i + 1) + ": start time should be before end time", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_add_slot){
            AddSlotView();
        }else if(id == R.id.btn_save){
            AddSlots();
        }
    }
}

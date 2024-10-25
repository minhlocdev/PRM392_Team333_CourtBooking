package com.example.prm392_team333_courtbooking.court_manage;

import static androidx.core.content.ContentProviderCompat.requireContext;

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

import com.example.prm392_team333_courtbooking.R;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Court;
import Models.CourtSlot;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;

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

    private void AddSlots() {

        if(!Validate( etCourtName, etAddress, etOpenTime, etClosedTime, etProvince)){
            return;
        }

        if(!ValidateTime(allSlots)){
            return;
        }

        if(OverLap(slotTimes)){
            return;
        }

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);
            int courtSlotId = Integer.parseInt(slot.getTag().toString());

            float cost;
            try{
                cost = Float.parseFloat(etCost.getText().toString());
            }catch (Exception e){
                etCost.setError("Invalid cost format (00.00)");
                return;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            CourtSlot courtSlot = courtSlotRepository.getSlotById(courtSlotId);

            if(courtSlot != null){
                courtSlotRepository.updateCourtSlot(courtSlotId, courtId, timeStart, timeEnd, cost);
            }else{
                courtSlotRepository.insertCourtSlot( courtId, timeStart, timeEnd, cost);
            }
        }


    }

    private boolean Validate(EditText etCourtName, EditText etAddress, EditText etOpenTime, EditText etClosedTime, EditText etProvince){

        // Validate court name
        if (etCourtName.getText().toString().trim().isEmpty() || etCourtName.getText().toString().isBlank()) {
            etCourtName.setError("Court name is required");
            etCourtName.requestFocus();
            return false;
        }

        // Validate address
        if (etAddress.getText().toString().trim().isEmpty() || etAddress.getText().toString().isBlank()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }

        // Validate province
        if (etProvince.getText().toString().trim().isEmpty() || etProvince.getText().toString().isBlank()) {
            etProvince.setError("Province is required");
            etProvince.requestFocus();
            return false;
        }


        if(etOpenTime.getText().toString().trim().isEmpty() || etOpenTime.getText().toString().isBlank()){
            etOpenTime.setError("Open time is required");
            etOpenTime.requestFocus();
            return false;
        }

        if(etClosedTime.getText().toString().trim().isEmpty() || etClosedTime.getText().toString().isBlank()){
            etClosedTime.setError("Closed time is required");
            etClosedTime.requestFocus();
            return false;
        }

        try{
            if(!LocalTime.parse(etOpenTime.getText().toString()).isBefore(LocalTime.parse(etClosedTime.getText().toString()))){
                Toast.makeText(this, "Open time is bigger than closed time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
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

        newSlot.setTag(0);
        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Check if the selected minute is 00 or 30
                    if (selectedMinute == 0 || selectedMinute == 30) {
                        // Format and display the valid selected time in the EditText
                        timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    } else {
                        // Show an error message if the selected minute is not 00 or 30
                        Toast.makeText(this, "Please select a time with minutes as 00 or 30 only.", Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, true);

        timePickerDialog.show();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.et_opening_time){
            showTimePickerDialog(etOpenTime);
        }else if(id== R.id.et_closing_time){
            showTimePickerDialog(etClosedTime);
        }else if(id == R.id.btn_add_slot){
            AddSlotView();
        }else if(id == R.id.btn_save){
            AddSlots();
        }
    }
}

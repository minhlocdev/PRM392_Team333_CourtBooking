package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import static Constant.SessionConstant.courtOwner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_court_manage;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_layout;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.CourtOwner;
import Models.CourtSlot;
import Repository.CourtOwnerRepository;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;
import Session.SessionManager;

public class AddCourt extends Fragment implements View.OnClickListener {
    private EditText et_court_name, et_address, et_open_time, et_closed_time, et_province;
    private LinearLayout slotContainer;
    private ImageButton btnAddSlot;
    private Button btnDone;

    private CourtOwnerRepository courtOwnerRepository;
    private CourtRepository courtRepository;
    private CourtSlotRepository courtSlotRepository;

    private String phoneNumber;
    private final List<View> allSlots = new ArrayList<>();
    List<LocalTime[]> slotTimes = new ArrayList<>();

    private int hour, minute;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_court_layout, container, false);

        // Initialize views
        et_court_name = view.findViewById(R.id.et_court_name);
        et_address = view.findViewById(R.id.et_address);
        et_open_time = view.findViewById(R.id.et_opening_time);
        et_closed_time = view.findViewById(R.id.et_closing_time);
        et_province = view.findViewById(R.id.et_province);
        btnAddSlot = view.findViewById(R.id.btn_add_slot);
        btnDone = view.findViewById(R.id.btn_save);
        slotContainer = view.findViewById(R.id.slotContainer);

        AddSlotView(view); // Initial slot

        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phoneNumber");
        }

        courtOwnerRepository = new CourtOwnerRepository(requireContext());
        courtRepository = new CourtRepository(requireContext());
        courtSlotRepository = new CourtSlotRepository(requireContext());

        et_open_time.setOnClickListener(this);
        et_closed_time.setOnClickListener(this);
        btnAddSlot.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        return view;
    }

    private boolean AddSlots() {
        if (!ValidateInputs()) {
            return false;
        }

        SessionManager sessionManager = new SessionManager(requireContext(), courtOwner);
        long courtId = courtRepository.insertCourt(
                sessionManager.getCourtOwnerId(),
                et_court_name.getText().toString(),
                et_open_time.getText().toString(),
                et_closed_time.getText().toString(),
                et_province.getText().toString(),
                et_address.getText().toString(),
                "OPEN",
                null
        );

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

            LocalTime startTime, endTime;
            try {
                startTime = LocalTime.parse(timeStart);
                endTime = LocalTime.parse(timeEnd);
                if (!isValidMinute(startTime) || !isValidMinute(endTime)) {
                    Toast.makeText(requireContext(), "Please select times with minutes as 00 or 30 only.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (DateTimeParseException e) {
                Toast.makeText(requireContext(), "Invalid time format.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (java.time.Duration.between(startTime, endTime).toMinutes() > 90) {
                Toast.makeText(requireContext(), "Time duration cannot exceed 90 minutes.", Toast.LENGTH_SHORT).show();
                return false;
            }

            courtSlotRepository.insertCourtSlot((int) courtId, timeStart, timeEnd, cost);
        }
        return true;
    }
    private void AddSlotView(View view){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        int index = slotContainer.indexOfChild(view.findViewById(R.id.btn_add_slot));

        newSlot.setTag(0);
        // Add the inflated slot layout to the container
        slotContainer.addView(newSlot, index);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    private boolean OverLap(List<LocalTime[]> slotTimes) {
        for (int i = 0; i < slotTimes.size(); i++) {
            LocalTime[] currentSlot = slotTimes.get(i);
            for (int j = i + 1; j < slotTimes.size(); j++) {
                if (currentSlot[0].isBefore(slotTimes.get(j)[1]) && slotTimes.get(j)[0].isBefore(currentSlot[1])) {
                    Toast.makeText(getContext(), "Slot " + (i + 1) + " overlaps with Slot " + (j + 1), Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ValidateInputs() {
        // Basic validation for input fields (name, address, open/close times, province)
        if (et_court_name.getText().toString().isEmpty() || et_address.getText().toString().isEmpty()
                || et_province.getText().toString().isEmpty() || et_open_time.getText().toString().isEmpty()
                || et_closed_time.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            LocalTime openTime = LocalTime.parse(et_open_time.getText().toString());
            LocalTime closeTime = LocalTime.parse(et_closed_time.getText().toString());
            if (!openTime.isBefore(closeTime)) {
                Toast.makeText(getContext(), "Open time must be before close time.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!isValidMinute(openTime) || !isValidMinute(closeTime)) {
                Toast.makeText(getContext(), "Open and Close times must be on the hour or half-hour.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (DateTimeParseException e) {
            Toast.makeText(getContext(), "Invalid time format. Use HH:mm.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showTimePickerDialog(final EditText timeField) {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (TimePicker view, int selectedHour, int selectedMinute) -> {
            if (selectedMinute == 0 || selectedMinute == 30) {
                timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            } else {
                Toast.makeText(getContext(), "Please select a time with minutes as 00 or 30 only.", Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private boolean isValidMinute(LocalTime time) {
        int minute = time.getMinute();
        return minute == 0 || minute == 30;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.et_opening_time) {
            showTimePickerDialog(et_open_time);
        } else if (id == R.id.et_closing_time) {
            showTimePickerDialog(et_closed_time);
        } else if (id == R.id.btn_add_slot) {
            AddSlotView(view);
        } else if (id == R.id.btn_save) {
            if (AddSlots()) {
                Toast.makeText(requireContext(), "Court Added Successfully", Toast.LENGTH_SHORT).show();

                // Navigate back to court_owner_court_manage
                Fragment courtManageFragment = new court_owner_court_manage();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, courtManageFragment);
                transaction.addToBackStack(null); // Optional: Adds to the back stack
                transaction.commit();
            }
        }
    }
}
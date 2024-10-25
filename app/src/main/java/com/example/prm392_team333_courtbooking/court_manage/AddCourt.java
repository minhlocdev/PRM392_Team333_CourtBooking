package com.example.prm392_team333_courtbooking.court_manage;

import static Constant.SessionConstant.courtOwner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import Session.SessionManager;

public class AddCourt extends Fragment implements View.OnClickListener {
    private EditText et_court_name;
    private EditText et_address;
    private EditText et_open_time;
    private EditText et_closed_time;
    private EditText et_province;
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

    @Nullable
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

        // Add first slot
        AddSlotView(view);

        // Get arguments if available (from activity or previous fragment)
        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phoneNumber");
        }

        // Initialize repositories
        courtOwnerRepository = new CourtOwnerRepository(requireContext());
        courtRepository = new CourtRepository(requireContext());
        courtSlotRepository = new CourtSlotRepository(requireContext());

        // Set click listeners
        et_open_time.setOnClickListener(this);
        et_closed_time.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnAddSlot.setOnClickListener(this);

        return view;
    }

    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format and display the selected time in the EditText
                    timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private boolean AddSlots() {
        // Validate input fields
        if (OverLap(slotTimes)) {
            return false;
        }

        SessionManager sessionManager = new SessionManager(requireContext(), courtOwner);

        long courtId = courtRepository.insertCourt(sessionManager.getCourtOwnerId(), et_court_name.getText().toString(),
                et_open_time.getText().toString(), et_closed_time.getText().toString(),
                et_province.getText().toString(), et_address.getText().toString(), "OPEN", null);

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            float cost;
            try {
                cost = Float.parseFloat(etCost.getText().toString());
            } catch (Exception e) {
                etCost.setError("Invalid cost format (00.00)");
                return false;
            }

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();

            courtSlotRepository.insertCourtSlot((int) courtId, timeStart, timeEnd, cost);
        }

        return true;
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
                    Toast.makeText(requireContext(), "Slot " + (i + 1) + " overlaps with Slot " + (j + 1), Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }

        return false;
    }

    private void AddSlotView(View view) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View newSlot = inflater.inflate(R.layout.court_slot_item, slotContainer, false);

        EditText etTimeStart = newSlot.findViewById(R.id.et_time_start);
        EditText etTimeEnd = newSlot.findViewById(R.id.et_time_end);

        etTimeStart.setOnClickListener(v -> showTimePickerDialog(etTimeStart));
        etTimeEnd.setOnClickListener(v -> showTimePickerDialog(etTimeEnd));

        int index = slotContainer.indexOfChild(view.findViewById(R.id.btn_add_slot));

        slotContainer.addView(newSlot, index);
        allSlots.add(newSlot);
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
                // Navigate or close fragment
                Toast.makeText(requireContext(), "Court Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

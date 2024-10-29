package com.example.prm392_team333_courtbooking.fragements.player.player_search;

import static Constant.SessionConstant.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player.login.LoginForPlayers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;

import Adapter.CourtSlotAdapter;
import Models.Booking;
import Models.CourtSlot;
import Repository.BookingRepository;
import Repository.CourtSlotRepository;
import Session.SessionManager;

public class BookingDialog extends DialogFragment {

    private CalendarView cvDate;

    private EditText etTimeStart;

    private EditText etTimeEnd;

    private int courtId;

    private CourtSlotRepository courtSlotRepository;

    private BookingRepository bookingRepository;

    private SessionManager sessionManager;

    private String selectedDate;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_booking, container, false);

        etTimeStart = view.findViewById(R.id.et_time_start);
        etTimeEnd = view.findViewById(R.id.et_time_end);
        cvDate = view.findViewById(R.id.calendarView);
        Button btnDone = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        RecyclerView rvSlotTime = view.findViewById(R.id.recyclerViewTimeSlots);


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvSlotTime.setLayoutManager(layoutManager);

        sessionManager = new SessionManager(requireContext(), user);

        if(!sessionManager.isLoggedInUser()){
            Intent intent = new Intent(requireContext(), LoginForPlayers.class);
            startActivity(intent);
        }


        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        cvDate.setMinDate(today);

        cvDate.setDate(today, true, true);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        selectedDate = String.format("%04d-%02d-%02d", year, month, day);

        courtSlotRepository = new CourtSlotRepository(getContext());

        bookingRepository = new BookingRepository(getContext());

        if (getArguments() != null) {
            courtId = getArguments().getInt("courtId");
        }

        List<CourtSlot> courtSlots = courtSlotRepository.getSlotsByCourtId(courtId);

        CourtSlotAdapter adapter = new CourtSlotAdapter(courtSlots);
        rvSlotTime.setAdapter(adapter);

        btnDone.setOnClickListener(v -> Save());

        btnCancel.setOnClickListener(v -> dismiss());

        etTimeStart.setOnClickListener(v -> showTimePickerDialog(etTimeStart));

        etTimeEnd.setOnClickListener(v -> showTimePickerDialog(etTimeEnd));

        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                 selectedDate = String.format("%04d-%02d-%02d",
                        year, month + 1, dayOfMonth);

            }
        });

        return view;

    }

    private void Save(){

        String timeStart = etTimeStart.getText().toString();
        String timeEnd = etTimeEnd.getText().toString();

        boolean checkTimeInput = ValidateTime( timeStart, timeEnd);

        if(!checkTimeInput){
            return;
        }

        boolean isOverlap = IsOverLap(selectedDate, timeStart, timeEnd);

        if(isOverlap){
            etTimeStart.setError("There are bookings in this range time");
            etTimeStart.requestFocus();
            etTimeEnd.setError("There are bookings in this range time");
            etTimeStart.requestFocus();
            return;
        }

        String createdAt = LocalDateTime.now().toString();

        List<CourtSlot> courtSlotList = courtSlotRepository.getSlotsByCourtIdAndTime(courtId, timeStart, timeEnd);

        if(!validateBookingTime(courtSlotList, timeStart, timeEnd)){
            return;
        }


        float totalPrice = calculateTotalPrice(courtSlotList, timeStart, timeEnd);

        bookingRepository.insertBooking(courtId, sessionManager.getUserId(), selectedDate, timeStart, timeEnd, totalPrice, "PENDING", createdAt, "");

        Toast.makeText(getContext(), "Your booking is successful", Toast.LENGTH_SHORT).show();

        dismiss();

    }

    private void showTimePickerDialog(final EditText timeField) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog( getContext(),
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format and display the selected time in the EditText
                    timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private boolean ValidateTime(String timeStart, String timeEnd){
        etTimeStart.setError(null);
        etTimeEnd.setError(null);


            if(timeStart.isEmpty() || timeStart.isBlank()){
                etTimeStart.setError("Time start cannot be empty");
                return false;
            }

            if (timeEnd.isEmpty()) {
                etTimeEnd.setError("Time end cannot be empty");
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

            }
            catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
            }

        return true;
    }

    private boolean IsOverLap(String date, String timeStart, String timeEnd) {
        etTimeStart.setError(null);
        etTimeEnd.setError(null);
        // Retrieve existing slots for this court on the selected date
        List<Booking> existingBookings = bookingRepository.getBookingsByCourtIdAndDate(courtId, date);

        // Parse the start and end times of the new booking
        LocalTime newStartTime = LocalTime.parse(timeStart);
        LocalTime newEndTime = LocalTime.parse(timeEnd);

        // Iterate through existing slots and check for overlap
        for (Booking booking : existingBookings) {
            LocalTime existingStartTime = LocalTime.parse(booking.getStartTime());
            LocalTime existingEndTime = LocalTime.parse(booking.getEndTime());

            // Check if the new slot overlaps with the existing slot
            if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                return true;
            }
        }

        return false;
    }

    private float calculateTotalPrice(List<CourtSlot> courtSlotList, String timeStart, String timeEnd) {
        LocalTime newStartTime = LocalTime.parse(timeStart);
        LocalTime newEndTime = LocalTime.parse(timeEnd);
        float totalPrice = 0;

        for (CourtSlot slot : courtSlotList) {
            LocalTime slotStartTime = LocalTime.parse(slot.getTimeStart());
            LocalTime slotEndTime = LocalTime.parse(slot.getTimeEnd());

            // Calculate the overlapping time
            LocalTime effectiveStart = newStartTime.isBefore(slotStartTime) ? slotStartTime : newStartTime;
            LocalTime effectiveEnd = newEndTime.isAfter(slotEndTime) ? slotEndTime : newEndTime;

            // Check if there's an actual overlap
            if (effectiveStart.isBefore(effectiveEnd)) {
                // Calculate the duration in hours
                long minutes = Duration.between(effectiveStart, effectiveEnd).toMinutes();
                // Calculate the price for this duration
                double cost = slot.getCost();
                double hours = (double) minutes /60;
                totalPrice += (float) (hours * cost); // Assuming you have a getPricePerHour() method in CourtSlot
            }
        }

        return totalPrice;
    }

    private boolean validateBookingTime(List<CourtSlot> courtSlotList, String timeStart, String timeEnd) {
        etTimeStart.setError(null);
        etTimeEnd.setError(null);
        LocalTime newStartTime = LocalTime.parse(timeStart);
        LocalTime newEndTime = LocalTime.parse(timeEnd);

        boolean startInSlot = false;
        boolean endInSlot = false;

        // Variables to track the slots for checking gaps
        LocalTime lastEndTime = null;

        for (CourtSlot slot : courtSlotList) {
            LocalTime slotStartTime = LocalTime.parse(slot.getTimeStart());
            LocalTime slotEndTime = LocalTime.parse(slot.getTimeEnd());

            // Check if timeStart is in the current slot
            if ((newStartTime.equals(slotStartTime)|| newStartTime.isAfter(slotStartTime)) && newStartTime.isBefore(slotEndTime)) {
                startInSlot = true;
            }

            // Check if timeEnd is in the current slot
            if (newEndTime.equals(slotStartTime) || newEndTime.isAfter(slotStartTime) && newEndTime.isBefore(slotEndTime)) {
                endInSlot = true;
            }

            // Check for gaps between slots
            if (lastEndTime != null && lastEndTime.isBefore(slotStartTime)) {
                // If there's a gap, and if the new times are within that gap
                if (newStartTime.isAfter(lastEndTime) && newStartTime.isBefore(slotStartTime)) {
                    etTimeStart.setError("The selected time range has gaps without available slots.");
                    return false;
                }
            }

            // Update lastEndTime
            lastEndTime = slotEndTime;
        }

        // Final check for timeStart and timeEnd
        if (!startInSlot) {
            etTimeStart.setError("Time start must be within an available slot.");
            return false;
        }

        if (!endInSlot) {
            etTimeEnd.setError("Time end must be within an available slot.");
            return false;
        }

        // Final check for the end time against the last slot
        if (newEndTime.isAfter(lastEndTime)) {
            etTimeStart.setError("The selected time range extends beyond the available slots.");
            etTimeEnd.setError("The selected time range extends beyond the available slots.");
            return false;
        }

        return true;
    }


}

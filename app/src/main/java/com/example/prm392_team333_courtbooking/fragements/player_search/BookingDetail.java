package com.example.prm392_team333_courtbooking.fragements.player_search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.prm392_team333_courtbooking.Interface.BookingDialogListener;
import com.example.prm392_team333_courtbooking.R;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;

import Models.Booking;
import Models.Court;
import Models.CourtSlot;
import Repository.BookingRepository;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;

public class BookingDetail extends DialogFragment {

    private ImageView ivCourtImage;

    private ImageButton ibCalendar;

    private TextView tvCourtName, tvCourtTime, tvAddress, tvDate, tvCost, tvStatus;

    private Button btnSave;

    private EditText etTimeStart, etTimeEnd;

    private BookingRepository  bookingRepository;

    private CourtRepository courtRepository;

    private CourtSlotRepository courtSlotRepository;

    private int bookingId;

    private int courtId;

    private int playerId;

    private BookingDialogListener listener;

    public void setListener(BookingDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_detail_layout, container, false);

        ivCourtImage = view.findViewById(R.id.iv_court_image);
        ibCalendar = view.findViewById(R.id.ib_calendar);
        tvCourtName = view.findViewById(R.id.tv_court_name);
        tvCourtTime = view.findViewById(R.id.tv_court_time);
        tvAddress = view.findViewById(R.id.tv_address);
        tvDate = view.findViewById(R.id.tv_date);
        tvCost = view.findViewById(R.id.tv_cost);
        tvStatus = view.findViewById(R.id.tv_status);
        btnSave = view.findViewById(R.id.btn_save);

        etTimeStart = view.findViewById(R.id.et_time_start);
        etTimeEnd = view.findViewById(R.id.et_time_end);

        bookingRepository = new BookingRepository(getContext());
        courtRepository = new CourtRepository(getContext());
        courtSlotRepository = new CourtSlotRepository(getContext());

        if (getArguments() != null) {
            bookingId = getArguments().getInt("bookingId");
        }

        etTimeStart.setOnClickListener(v -> {
            showTimePickerDialog(etTimeStart);
        });

        etTimeEnd.setOnClickListener(v -> {
            showTimePickerDialog(etTimeEnd);
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });

        LoadData();

        return view;

    }

    private void Save(){
        String timeStart = etTimeStart.getText().toString().trim();
        String timeEnd = etTimeEnd.getText().toString().trim();
        String date = tvDate.getText().toString().trim();

        ValidateTime(timeStart, timeEnd);

        boolean isOverlap = IsOverLap(date, timeStart, timeEnd);

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

        int id =  bookingRepository.updateBooking(bookingId, courtId, playerId, date, timeStart, timeEnd, totalPrice, "PENDING");

        LoadData();

        if (listener != null) {
            listener.onBookingSaved(date);
        }
    }

    private void LoadData(){
        Booking booking = bookingRepository.getBookingsById(bookingId);
        Court court = courtRepository.getCourtById(booking.getCourtId());

        playerId = booking.getPlayerId();

        courtId = court.getCourtId();

        tvCourtName.setText(court.getCourtName());


        byte[] courtImage = court.getImage();
        if (courtImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(courtImage, 0, courtImage.length);
            ivCourtImage.setImageBitmap(bitmap);
        } else {
            ivCourtImage.setImageResource(R.drawable.old_trafford);
        }


        tvAddress.setText(court.getAddress() + " - " + court.getProvince());

        tvDate.setText(booking.getBookingDate());
        tvCost.setText(String.valueOf(booking.getPrice()));
        tvCourtTime.setText(court.getOpenTime() + " - " + court.getClosedTime());
        etTimeStart.setText(booking.getStartTime());
        etTimeEnd.setText(booking.getEndTime());

        if(booking.getStatus().equals("BOOKED")){
            tvStatus.setText("BOOKED");
            int color = ContextCompat.getColor(getContext(), R.color.booked);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
            } else {
                // For lower API levels, you might want to set a different approach
                Drawable background = tvStatus.getBackground();
                if (background != null) {
                    background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        else if(booking.getStatus().equals("PENDING")){
            tvStatus.setText("PENDING");
            int color = ContextCompat.getColor(getContext(), R.color.pending);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
            } else {
                // For lower API levels, you might want to set a different approach
                Drawable background = tvStatus.getBackground();
                if (background != null) {
                    background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        else if(booking.getStatus().equals("CANCEL")){
            tvStatus.setText("CANCEL");
            int color = ContextCompat.getColor(getContext(), R.color.crimson);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
            } else {
                // For lower API levels, you might want to set a different approach
                Drawable background = tvStatus.getBackground();
                if (background != null) {
                    background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
            }
        }

        setInitialDate(booking.getBookingDate());
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
            if(booking.getBookingId() != bookingId){
                // Check if the new slot overlaps with the existing slot
                if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                    return true;
                }
            }

        }

        return false;
    }

    private void setInitialDate(String bookingDate) {
        // Parse the booking date
        String[] dateParts = bookingDate.split("-");
        int day = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
        int year = Integer.parseInt(dateParts[0]);

        tvDate.setText(bookingDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Show DatePickerDialog with the initial date
        ibCalendar.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format and display the selected date in the TextView
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        tvDate.setText(selectedDate);
                    }, year, month, day); // Pass the parsed date

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });
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
                totalPrice += hours * cost; // Assuming you have a getPricePerHour() method in CourtSlot
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
            if (newStartTime.isAfter(slotStartTime) && newStartTime.isBefore(slotEndTime)) {
                startInSlot = true;
            }

            // Check if timeEnd is in the current slot
            if (newEndTime.isAfter(slotStartTime) && newEndTime.isBefore(slotEndTime)) {
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

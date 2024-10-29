package com.example.prm392_team333_courtbooking.fragements.player.player_search;

import static Constant.SessionConstant.user;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.Interface.BookingDialogListener;
import com.example.prm392_team333_courtbooking.R;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import Adapter.BookingAdapterForUsers;
import Models.Booking;
import Repository.BookingRepository;
import Session.SessionManager;

public class BookingFragment extends Fragment implements BookingDialogListener {

    private BookingRepository bookingRepository;
    private List<Booking> bookings;
    private BookingAdapterForUsers adapter;

    private SessionManager sessionManager;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);

        sessionManager = new SessionManager(requireContext(), user);

        // Set up the calendar to today's date
        calendarView.setDate(System.currentTimeMillis(), false, true);

        // Add listener to handle date selection from the CalendarView
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // CalendarView returns the month as zero-indexed, so we need to add 1 to it
            LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);

            // Convert LocalDate to a string in the format you need (for example, yyyy-MM-dd)
            String formattedDate = selectedDate.toString();

            // Load bookings based on the selected date
            loadBookings(formattedDate);

            // Notify the adapter that the data has changed
            adapter.setBookings(bookings);
            adapter.notifyDataSetChanged();
        });

        // Initialize RecyclerView
        RecyclerView rvBookings = view.findViewById(R.id.bookingRecyclerView);

        // Improve performance as layout size does not change
        rvBookings.setHasFixedSize(true);

        // Initialize BookingRepository and load initial data
        bookingRepository = new BookingRepository(getContext());

        // Load today's bookings
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        loadBookings(today.toString());

        // Initialize the adapter with bookings and set it to the RecyclerView
        adapter = new BookingAdapterForUsers(getContext(), bookings, R.layout.fragment_player_calendar_item, getParentFragmentManager(), this);
        rvBookings.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvBookings.setAdapter(adapter);

        return view;
    }

    private void loadBookings(String date) {
        // Fetch bookings for the selected player and date
        bookings = bookingRepository.getBookingsByPlayerIdAndDate(sessionManager.getUserId(), date);
        if (bookings == null) {
            bookings = new ArrayList<>(); // Initialize to avoid NullPointerException
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBookingSaved(String date) {
        bookings = bookingRepository.getBookingsByPlayerIdAndDate(sessionManager.getUserId(), date);
        adapter.setBookings(bookings);
        adapter.notifyDataSetChanged();
    }

}

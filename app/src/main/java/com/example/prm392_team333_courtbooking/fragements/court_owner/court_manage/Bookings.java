package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import static Constant.SessionConstant.courtOwner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_layout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import Adapter.BookingAdapterForCourtOwner;
import Models.Booking;
import Models.Court;
import Models.CourtListDown;
import Repository.BookingRepository;
import Repository.CourtRepository;
import Session.SessionManager;

public class Bookings extends Fragment {

    private BookingRepository bookingRepository;

    private CourtRepository courtRepository;

    private List<Booking> bookingList = new ArrayList<>();

    private RecyclerView rvBookings;

    private SessionManager sessionManager;

    private BookingAdapterForCourtOwner adapter;

    private int courtId;

    private String date;


    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Calendar"); // Set the title for this fragment
        ((court_owner_layout) requireActivity()).setNavigationButtonEnabled(false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.court_owner_bookings, container, false);

        courtId = 0;

        sessionManager = new SessionManager(requireContext(), courtOwner);

        bookingRepository = new BookingRepository(requireContext());

        courtRepository = new CourtRepository(requireContext());

        Spinner spinnerCourts = view.findViewById(R.id.spinnerCourts);


        List<CourtListDown> courts = courtRepository.getCourtsByCourtOwnerIdForListDown(sessionManager.getCourtOwnerId());
        CourtListDown courtListDown = new CourtListDown(0, "All");
        courts.add(0, courtListDown);

        ArrayAdapter<CourtListDown> adapterCourt = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courts);
        adapterCourt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourts.setAdapter(adapterCourt);

        // default at "All"
        spinnerCourts.setSelection(0);

        spinnerCourts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CourtListDown selectedCourt = (CourtListDown) parent.getItemAtPosition(position);

                int courtID = selectedCourt.getCourtId();
                String courtName = selectedCourt.getCourtName();

                courtId = courtID;

                loadData(date, courtId);

                adapter.setBookingList(bookingList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        CalendarView calendarView = view.findViewById(R.id.calendarView);

        rvBookings = view.findViewById(R.id.bookingRecyclerView);

        rvBookings.setLayoutManager(new LinearLayoutManager(requireContext()));

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // CalendarView returns the month as zero-indexed, so we need to add 1 to it
            LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);

            // Convert LocalDate to a string in the format you need (for example, yyyy-MM-dd)
            date = selectedDate.toString();

            // Load bookings based on the selected date
            loadData(date, courtId);

            adapter.setBookingList(bookingList);
            adapter.notifyDataSetChanged();

            // Notify the adapter that the data has changed
            adapter.setBookingList(bookingList);
            adapter.notifyDataSetChanged();
        });

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        date = today.toString();
        loadData(date, courtId);

        adapter = new BookingAdapterForCourtOwner(requireContext(), bookingList, getParentFragmentManager());
        rvBookings.setAdapter(adapter);

        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadData(String selectedDate, int courtId){
        bookingList = new ArrayList<>();

        if(courtId == 0){
            List<Court> courts = courtRepository.getCourtsByCourtOwnerId(sessionManager.getCourtOwnerId());

            for (Court c: courts) {
                List<Booking> bookings = bookingRepository.getBookingsByCourtIdAndDateForCourtOwner(c.getCourtId(), selectedDate);
                bookingList.addAll(bookings);
            }
        }else{
            List<Booking> bookings = bookingRepository.getBookingsByCourtIdAndDateForCourtOwner(courtId, selectedDate);
            bookingList.addAll(bookings);
        }



        if (bookingList == null) {
            bookingList = new ArrayList<>(); // Initialize to avoid NullPointerException
        }

    }
}

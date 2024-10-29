package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import static Constant.SessionConstant.courtOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_layout;
import com.example.prm392_team333_courtbooking.fragements.court_owner.login.LoginForCourtOwner;

import java.util.List;

import Adapter.CourtAdapterForHomePage;
import Adapter.CourtAdapterForUsers;
import Models.Court;
import Models.CourtOwner;
import Repository.BookingRepository;
import Repository.CourtOwnerRepository;
import Repository.CourtRepository;
import Session.SessionManager;

public class CourtListHome extends Fragment{
    private SessionManager sessionManager;
    private TextView userName, yesterdayText, thisMonthText, lastMonthText, todayText, numberOfBookingText;
    // Search - Dia Diem
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_court_owner_court_list_home, container, false);
        userName = view.findViewById(R.id.userName);

        CourtOwnerRepository ownerRepository = new CourtOwnerRepository(getContext());
        sessionManager = new SessionManager(requireContext(), courtOwner);
        int courtOwnerId = sessionManager.getCourtOwnerId();
        if (courtOwnerId == -1) { // Assuming -1 indicates no logged-in user
            Intent intent = new Intent(getActivity(), LoginForCourtOwner.class); // Replace with your LoginActivity class
            sessionManager.clearSession();
            startActivity(intent);
            getActivity().finish();
            return view;
        } else {
            CourtOwner owner = ownerRepository.getCourtOwnerById(courtOwnerId);
            userName.setText(owner.getFullName());
        }
        // Initialize RecyclerView
        RecyclerView courtRecyclerView = view.findViewById(R.id.courtRecyclerView);

        yesterdayText = view.findViewById(R.id.yesterdayText);
        thisMonthText = view.findViewById(R.id.thisMonthText);
        lastMonthText = view.findViewById(R.id.lastMonthText);
        todayText = view.findViewById(R.id.todayText);
        numberOfBookingText = view.findViewById(R.id.numberOfBookingText);

        // Improve performance as layout size does not change
        courtRecyclerView.setHasFixedSize(true);

        // Initialize CourtRepository and load courts
        BookingRepository bookingRepository = new BookingRepository(getContext());
        displayBookingStatistics(bookingRepository);

        CourtRepository courtRepository = new CourtRepository(getContext());
        List<Court> courtList = courtRepository.getCourtsByCourtOwnerId(courtOwnerId); // Fetch all courts from the repository


        // Set up the adapter with the court list
        CourtAdapterForHomePage courtAdapter = new CourtAdapterForHomePage(requireContext(), courtList, R.layout.fragment_court_owner_court_list_home_item, getParentFragmentManager());
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courtRecyclerView.setAdapter(courtAdapter);

        return view;
    }
    private void displayBookingStatistics(BookingRepository bookingRepository) {
        // Get total prices for different time frames
        double yesterdayPrice = bookingRepository.getPriceFromBooking("yesterday");
        double thisMonthPrice = bookingRepository.getPriceFromBooking("this_month");
        double lastMonthPrice = bookingRepository.getPriceFromBooking("last_month");
        double todayPrice = bookingRepository.getPriceFromBooking("today");

        // Get number of bookings today
        int todayBookingsCount = bookingRepository.getNumberOfBookings();

        // Update TextViews with fetched data
        yesterdayText.setText(String.format("%.3f VND", yesterdayPrice));
        thisMonthText.setText(String.format("%.3f VND", thisMonthPrice));
        lastMonthText.setText(String.format("%.3f VND", lastMonthPrice));
        todayText.setText(String.format("%.3f VND", todayPrice));
        numberOfBookingText.setText(String.format("%d times", todayBookingsCount));
    }
}

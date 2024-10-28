package com.example.prm392_team333_courtbooking.activities.owner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.Interface.TitleProvider;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.Bookings;

import java.util.Objects;

public class court_owner_calendar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public court_owner_calendar() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Calendar"); // Set the title for this fragment
        // Disable the navigation button
        ((court_owner_layout) requireActivity()).setNavigationButtonEnabled(false);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment player_search.
     */
    // TODO: Rename and change types and number of parameters
    public static court_owner_calendar newInstance(String param1, String param2) {
        court_owner_calendar fragment = new court_owner_calendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_court_owner_calendar, container, false);


        Bookings bookingFragment = new Bookings();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, bookingFragment);

        transaction.commit();

        return view;
    }
}

package com.example.prm392_team333_courtbooking.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player_search.BookingFragment;

public class player_calendar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public player_calendar() {
        // Required empty public constructor
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
    public static player_search newInstance(String param1, String param2) {
        player_search fragment = new player_search();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_bookings, container, false);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // on click button All
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, new BookingFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        return view;
    }



}

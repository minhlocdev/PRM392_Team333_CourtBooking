package com.example.prm392_team333_courtbooking.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player_search.AccountFragment;
import com.example.prm392_team333_courtbooking.fragements.player_search.AllFragment;
import com.example.prm392_team333_courtbooking.fragements.player_search.CourtFragment;
import com.example.prm392_team333_courtbooking.fragements.player_search.LocationFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link player_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class player_search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public player_search() {
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_search, container, false);


        Button allFilterBtn = view.findViewById(R.id.allFilterBtn);
        Button accountFilterBtn = view.findViewById(R.id.accountFilterBtn);
        Button locationBtn = view.findViewById(R.id.Location);
        Button courtFilterBtn = view.findViewById(R.id.courtFilterBtn);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // on click button All
        allFilterBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new AllFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // on click button Account
        accountFilterBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new AccountFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // on click button Location
        locationBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new LocationFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // on click button Court
        courtFilterBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new CourtFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }



}
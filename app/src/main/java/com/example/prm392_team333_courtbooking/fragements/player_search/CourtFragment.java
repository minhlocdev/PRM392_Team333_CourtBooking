package com.example.prm392_team333_courtbooking.fragements.player_search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.CourtAdapter;
import Models.Court;
import Repository.CourtRepository;

public class CourtFragment extends Fragment {
    private RecyclerView courtRecyclerView;
    private CourtAdapter courtAdapter;
    private List<Court> courtList;
    private CourtRepository courtRepository;

    public CourtFragment(){

    }


    // Search - Dia Diem
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_search_court, container, false);

        // Initialize RecyclerView
        courtRecyclerView = view.findViewById(R.id.courtRecyclerView);

        // Improve performance as layout size does not change
        courtRecyclerView.setHasFixedSize(true);

        // Initialize CourtRepository and load courts
        courtRepository = new CourtRepository(getContext());
        courtList = courtRepository.getAllCourts(); // Fetch all courts from the repository


        // Set up the adapter with the court list
        courtAdapter = new CourtAdapter(getContext(), courtList, R.layout.fragment_player_search_court_item);
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courtRecyclerView.setAdapter(courtAdapter);

        return view;
    }
}

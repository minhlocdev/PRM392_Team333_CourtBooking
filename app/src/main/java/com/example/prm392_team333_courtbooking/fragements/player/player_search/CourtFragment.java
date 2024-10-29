package com.example.prm392_team333_courtbooking.fragements.player.player_search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import java.util.List;
import Adapter.CourtAdapterForUsers;
import Models.Court;
import Repository.CourtRepository;

public class CourtFragment extends Fragment {

    // Search - Dia Diem
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_search_court, container, false);

        // Initialize RecyclerView
        RecyclerView courtRecyclerView = view.findViewById(R.id.courtRecyclerView);

        // Improve performance as layout size does not change
        courtRecyclerView.setHasFixedSize(true);

        // Initialize CourtRepository and load courts
        CourtRepository courtRepository = new CourtRepository(getContext());
        List<Court> courtList = courtRepository.getAllCourts();

        courtList.removeIf(court ->  court.getStatus().equals("INACTIVE"));


        // Set up the adapter with the court list
        CourtAdapterForUsers courtAdapter = new CourtAdapterForUsers(requireContext(), courtList, R.layout.fragment_player_search_court_item, getParentFragmentManager());
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courtRecyclerView.setAdapter(courtAdapter);
        EditText editTextSearch = view.findViewById(R.id.editTextText);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                courtAdapter.filter(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }
}

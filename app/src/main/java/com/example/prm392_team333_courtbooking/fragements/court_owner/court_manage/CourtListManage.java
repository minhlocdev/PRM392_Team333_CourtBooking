package com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage;

import static Constant.SessionConstant.courtOwner;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392_team333_courtbooking.R;

import java.util.List;
import Adapter.CourtAdapter;
import Models.Court;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class CourtListManage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.court_list_manage, container, false);

        // Initialize repository and session manager
        CourtOwnerRepository courtOwnerRepository = new CourtOwnerRepository(requireContext());
        SessionManager sessionManager = new SessionManager(requireContext(), courtOwner);

        // Set up RecyclerView
        RecyclerView courtList = view.findViewById(R.id.myListView);
        courtList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Get court owner and courts
        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerById(sessionManager.getCourtOwnerId());
        List<Court> courts = courtOwnerRepository.getCourtsByCourtOwnerId(courtOwner.getCourtOwnerId());

        // Set adapter
        CourtAdapter adapter = new CourtAdapter(requireContext(), courts, R.layout.court_list_manage_item);
        courtList.setAdapter(adapter);

        return view;
    }
}

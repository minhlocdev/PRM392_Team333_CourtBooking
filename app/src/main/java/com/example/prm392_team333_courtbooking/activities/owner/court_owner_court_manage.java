package com.example.prm392_team333_courtbooking.activities.owner;

import static Constant.SessionConstant.courtOwner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.Interface.TitleProvider;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.AddCourt;

import java.util.List;

import Adapter.CourtAdapter;
import Models.Court;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class court_owner_court_manage extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public court_owner_court_manage() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Court Management"); // Set the title for this fragment
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
    public static court_owner_court_manage newInstance(String param1, String param2) {
        court_owner_court_manage fragment = new court_owner_court_manage();
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
        View view = inflater.inflate(R.layout.fragment_court_owner_court_manage, container, false);

        // Set up the button to add a court
        Button buttonAddCourt = view.findViewById(R.id.buttonAddCourt);
        buttonAddCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with your own logic to open the AddCourt fragment/activity
                Fragment addCourtFragment = new AddCourt(); // Create an instance of AddCourt
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, addCourtFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Initialize RecyclerView
        setupRecyclerView(view);

        return view;
    }
    private void setupRecyclerView(View view) {
        RecyclerView courtList = view.findViewById(R.id.myListView);
        courtList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize repository and session manager
        CourtOwnerRepository courtOwnerRepository = new CourtOwnerRepository(requireContext());
        SessionManager sessionManager = new SessionManager(requireContext(), courtOwner);

        // Get court owner and courts
        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerById(sessionManager.getCourtOwnerId());
        List<Court> courts = courtOwnerRepository.getCourtsByCourtOwnerId(courtOwner.getCourtOwnerId());

        // Set adapter
        CourtAdapter adapter = new CourtAdapter(requireContext(), courts, R.layout.court_list_manage_item);
        courtList.setAdapter(adapter);
    }

}

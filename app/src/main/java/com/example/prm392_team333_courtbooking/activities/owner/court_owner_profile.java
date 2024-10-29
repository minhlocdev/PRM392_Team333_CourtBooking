package com.example.prm392_team333_courtbooking.activities.owner;

import static Constant.SessionConstant.courtOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.Interface.OnLogoutListener;
import com.example.prm392_team333_courtbooking.Interface.TitleProvider;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.MainActivity;
import com.example.prm392_team333_courtbooking.fragements.court_owner.login.LoginForCourtOwner;

import Session.SessionManager;

public class court_owner_profile extends Fragment implements OnLogoutListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public court_owner_profile() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Profile"); // Set the title for this fragment
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
    public static court_owner_profile newInstance(String param1, String param2) {
        court_owner_profile fragment = new court_owner_profile();
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
        View view = inflater.inflate(R.layout.fragment_court_owner_profile, container, false);


        com.example.prm392_team333_courtbooking.fragements.court_owner.profile.court_owner_profile profile = new com.example.prm392_team333_courtbooking.fragements.court_owner.profile.court_owner_profile();
        profile.setLogoutListener(this); // Set the listener
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profile);
        transaction.commit();
        return view;
    }
    @Override
    public void onLogout() {
        // Handle logout here, for example, clear session and navigate to login screen
        SessionManager sessionManager = new SessionManager(requireContext(), courtOwner);
        sessionManager.clearSession();

        Intent intent = new Intent(requireContext(), LoginForCourtOwner.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        requireActivity().finish(); // Finish the current activity
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}

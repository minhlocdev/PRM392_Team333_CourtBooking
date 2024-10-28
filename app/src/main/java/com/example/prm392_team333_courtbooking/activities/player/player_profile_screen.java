package com.example.prm392_team333_courtbooking.activities.player;

import static Constant.SessionConstant.courtOwner;
import static Constant.SessionConstant.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_team333_courtbooking.Interface.OnLogoutListener;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.MainActivity;
import com.example.prm392_team333_courtbooking.fragements.player.profile.player_profile;

import Session.SessionManager;

public class player_profile_screen extends Fragment implements OnLogoutListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public player_profile_screen() {
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
    public static player_profile_screen newInstance(String param1, String param2) {
        player_profile_screen fragment = new player_profile_screen();
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
        View view = inflater.inflate(R.layout.fragment_player_profile_screen, container, false);


        player_profile profile = new player_profile();
        profile.setLogoutListener(this); // Set the listener
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profile);
        transaction.commit();
        return view;
    }
    @Override
    public void onLogout() {
        // Handle logout here, for example, clear session and navigate to login screen
        SessionManager sessionManager = new SessionManager(requireContext(), user);
        sessionManager.clearSession();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        requireActivity().finish(); // Finish the current activity
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}

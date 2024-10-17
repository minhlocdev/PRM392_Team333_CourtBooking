package com.example.prm392_team333_courtbooking.fragements.player_search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import java.util.List;
import Adapter.UserAdapter;
import Models.User;
import Repository.UserRepository;

public class AccountFragment extends Fragment {
    public AccountFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_search_account, container, false);

        // Initialize RecyclerView
        RecyclerView userRecyclerView = view.findViewById(R.id.user_recyclerview);

        // Improve performance as layout size does not change
        userRecyclerView.setHasFixedSize(true);

        // Initialize CourtRepository and load courts
        UserRepository userRepository = new UserRepository(getContext());
        List<User> userList = userRepository.getAllUsers(); // Fetch all courts from the repository

        // Set up RecyclerView
        UserAdapter userAdapter = new UserAdapter(getContext(), userList);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        userRecyclerView.setAdapter(userAdapter);

        return view;
    }
}

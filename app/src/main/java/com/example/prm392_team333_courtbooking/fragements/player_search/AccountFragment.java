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

import java.util.ArrayList;
import java.util.List;

import ListAdapter.CourtAdapter;
import ListAdapter.UserAdapter;
import Models.Court;
import Models.User;

public class AccountFragment extends Fragment {
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    public AccountFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_search_account, container, false);

        // Initialize RecyclerView
        userRecyclerView = view.findViewById(R.id.user_recyclerview);

        // Improve performance as layout size does not change
        userRecyclerView.setHasFixedSize(true);

        // Khởi tạo danh sách trước khi thêm user
        userList = new ArrayList<>();

        // Add Manchester United football players to the list
        userList.add(new User(1, "d_degea", "David de Gea", "degea@mu.com", "0123456789", "Goalkeeper",  true));
        userList.add(new User(2, "r_varane", "Raphaël Varane", "varane@mu.com", "0123456789", "Defender",  true));
        userList.add(new User(3, "l_martinez", "Lisandro Martínez", "martinez@mu.com", "0123456789", "Defender", true));
        userList.add(new User(4, "l_shaw", "Luke Shaw", "shaw@mu.com", "0123456789", "Defender",  true));
        userList.add(new User(5, "b_fernandes", "Bruno Fernandes", "fernandes@mu.com", "0123456789", "Midfielder",  true));
        userList.add(new User(6, "m_mount", "Mason Mount", "mount@mu.com", "0123456789", "Midfielder",  true));
        userList.add(new User(7, "m_rashford", "Marcus Rashford", "rashford@mu.com", "0123456789", "Forward",  true));
        userList.add(new User(8, "a_martial", "Anthony Martial", "martial@mu.com", "0123456789", "Forward",  true));
        userList.add(new User(9, "casemiro", "Casemiro", "casemiro@mu.com", "0123456789", "Midfielder",  true));
        userList.add(new User(10, "h_maguire", "Harry Maguire", "maguire@mu.com", "0123456789", "Defender",  true));
        userList.add(new User(11, "a_onana", "André Onana", "onana@mu.com", "0123456789", "Goalkeeper",  true));

        // Set up RecyclerView
        userAdapter = new UserAdapter(getContext(), userList);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        userRecyclerView.setAdapter(userAdapter);

        return view;
    }
}

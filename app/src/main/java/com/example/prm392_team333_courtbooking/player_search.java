package com.example.prm392_team333_courtbooking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ListAdapter.CourtAdapter;
import Models.Court;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link player_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class player_search extends Fragment {
    private RecyclerView courtRecyclerView;
    private CourtAdapter courtAdapter;
    private List<Court> courtList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_search, container, false);

        // Initialize RecyclerView
        courtRecyclerView = view.findViewById(R.id.courtRecyclerView);

        // Improve performance as layout size does not change
        courtRecyclerView.setHasFixedSize(true);

        // Initialize the court list and add sample stadium data
        courtList = new ArrayList<>();
        courtList.add(new Court(1, 100, "Available", "Avenida de Concha Espina 1, 28036 Madrid, Spain", "Madrid", "Santiago Bernabéu", 1));
        courtList.add(new Court(2, 101, "Booked", "Carrer d'Aristides Maillol, 12, 08028 Barcelona, Spain", "Barcelona", "Camp Nou", 1));
        courtList.add(new Court(3, 102, "Under Maintenance", "Wembley, London HA9 0WS, United Kingdom", "London", "Wembley Stadium", 1));
        courtList.add(new Court(4, 103, "Available", "Werner-Heisenberg-Allee 25, 80939 München, Germany", "Munich", "Allianz Arena", 1));
        courtList.add(new Court(5, 104, "Booked", "Piazza dello Sport, 1, 20151 Milano MI, Italy", "Milan", "San Siro", 1));
        courtList.add(new Court(6, 105, "Available", "Strobelallee 50, 44139 Dortmund, Germany", "Dortmund", "Signal Iduna Park", 1));
        courtList.add(new Court(7, 106, "Available", "Rua de Dr. António Ferreira, 4350-420 Porto, Portugal", "Porto", "Estadio do Dragão", 1));
        courtList.add(new Court(8, 107, "Booked", "Stade de France, 93216 Saint-Denis, France", "Saint-Denis", "Stade de France", 1));
        courtList.add(new Court(9, 108, "Under Maintenance", "Lansdowne Rd, Dublin 4, Ireland", "Dublin", "Aviva Stadium", 1));
        courtList.add(new Court(10, 109, "Available", "Kerepesi út 5, 1089 Budapest, Hungary", "Budapest", "Puskás Aréna", 1));

        courtAdapter = new CourtAdapter(getContext(), courtList);
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        courtRecyclerView.setAdapter(courtAdapter);

        return view;
    }
}
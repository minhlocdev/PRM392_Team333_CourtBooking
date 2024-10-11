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
import Models.Court;

public class CourtFragment extends Fragment {
    private RecyclerView courtRecyclerView;
    private CourtAdapter courtAdapter;
    private List<Court> courtList;
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

        // Initialize the court list and add sample stadium data
        courtList = new ArrayList<>();
        /*courtList.add(new Court(1, "Available", "Avenida de Concha Espina 1, 28036 Madrid, Spain", "Madrid", "Santiago Bernabéu", 1));
        courtList.add(new Court(2, "Booked", "Carrer d'Aristides Maillol, 12, 08028 Barcelona, Spain", "Barcelona", "Camp Nou", 1));
        courtList.add(new Court(3, "Under Maintenance", "Wembley, London HA9 0WS, United Kingdom", "London", "Wembley Stadium", 1));
        courtList.add(new Court(4, "Available", "Werner-Heisenberg-Allee 25, 80939 München, Germany", "Munich", "Allianz Arena", 1));
        courtList.add(new Court(5, "Booked", "Piazza dello Sport, 1, 20151 Milano MI, Italy", "Milan", "San Siro", 1));
        courtList.add(new Court(6, "Available", "Strobelallee 50, 44139 Dortmund, Germany", "Dortmund", "Signal Iduna Park", 1));
        courtList.add(new Court(7, "Available", "Rua de Dr. António Ferreira, 4350-420 Porto, Portugal", "Porto", "Estadio do Dragão", 1));
        courtList.add(new Court(8,  "Booked", "Stade de France, 93216 Saint-Denis, France", "Saint-Denis", "Stade de France", 1));
        courtList.add(new Court(9, "Under Maintenance", "Lansdowne Rd, Dublin 4, Ireland", "Dublin", "Aviva Stadium", 1));
        courtList.add(new Court(10,  "Available", "Kerepesi út 5, 1089 Budapest, Hungary", "Budapest", "Puskás Aréna", 1));*/

        courtAdapter = new CourtAdapter(getContext(), courtList);
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courtRecyclerView.setAdapter(courtAdapter);

        return view;
    }
}

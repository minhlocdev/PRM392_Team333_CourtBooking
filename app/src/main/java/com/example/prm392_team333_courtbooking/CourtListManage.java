package com.example.prm392_team333_courtbooking;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Adapter.CourtAdapter;
import Models.Court;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Repository.CourtRepository;
import Repository.CourtSlotRepository;

public class CourtListManage extends AppCompatActivity{

    private RecyclerView courtList;

    private String phoneNumber;

    private CourtRepository courtRepository;
    private CourtSlotRepository courtSlotRepository;
    private CourtOwnerRepository courtOwnerRepository;

    private CourtAdapter adapter;

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.court_list_manage);

        courtRepository = new CourtRepository(this);
        courtSlotRepository = new CourtSlotRepository(this);
        courtOwnerRepository = new CourtOwnerRepository(this);

        courtList = findViewById(R.id.myListView);

        courtList.setLayoutManager(new LinearLayoutManager(this));


        phoneNumber = getIntent().getStringExtra("phoneNumber");

        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerByPhoneNumber(phoneNumber);

        List<Court> courts = courtOwnerRepository.getCourtsByCourtOwnerId(courtOwner.getCourtOwnerId());

        adapter = new CourtAdapter(this, courts, R.layout.court_list_manage_item);
        courtList.setAdapter(adapter);
    }
}

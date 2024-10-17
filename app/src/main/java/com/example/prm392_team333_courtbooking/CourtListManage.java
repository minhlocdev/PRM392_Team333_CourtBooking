package com.example.prm392_team333_courtbooking;

import static Constant.SessionConstant.courtOwner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import Adapter.CourtAdapter;
import Models.Court;
import Models.CourtOwner;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class CourtListManage extends AppCompatActivity{

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.court_list_manage);

        CourtOwnerRepository courtOwnerRepository = new CourtOwnerRepository(this);

        SessionManager sessionManager = new SessionManager(this, courtOwner);

        RecyclerView courtList = findViewById(R.id.myListView);

        courtList.setLayoutManager(new LinearLayoutManager(this));

        CourtOwner courtOwner = courtOwnerRepository.getCourtOwnerById(sessionManager.getCourtOwnerId());

        List<Court> courts = courtOwnerRepository.getCourtsByCourtOwnerId(courtOwner.getCourtOwnerId());

        CourtAdapter adapter = new CourtAdapter(this, courts, R.layout.court_list_manage_item);

        courtList.setAdapter(adapter);
    }
}

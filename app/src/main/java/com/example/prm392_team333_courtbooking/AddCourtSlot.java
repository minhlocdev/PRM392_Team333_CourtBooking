package com.example.prm392_team333_courtbooking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AddCourtSlot extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout slotContainer;
    private ImageButton btnAddSlot;
    private Button btnDone;

    private List<View> allSlots = new ArrayList<>();

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.add_court_slot_layout);

        btnAddSlot = findViewById(R.id.btn_add_slot);
        btnDone = findViewById(R.id.btn_done);
        slotContainer = findViewById(R.id.slotContainer);

        btnAddSlot.setOnClickListener(this);
        btnDone.setOnClickListener(this);

    }

    private void AddSlotView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newSlot = inflater.inflate(R.layout.add_court_slot_layout, null);

        // Add the new slot to the container
        slotContainer.addView(newSlot);

        // Add the new slot to the list for tracking
        allSlots.add(newSlot);
    }

    private void AddSlots(){
        if(!getSlotValues()){
            Toast.makeText(this, "Validation fail",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getSlotValues() {

        for (View slot : allSlots) {
            EditText etTimeStart = slot.findViewById(R.id.et_time_start);
            EditText etTimeEnd = slot.findViewById(R.id.et_time_end);
            EditText etCost = slot.findViewById(R.id.et_cost);

            String timeStart = etTimeStart.getText().toString();
            String timeEnd = etTimeEnd.getText().toString();
            String cost = etCost.getText().toString();

            if(timeStart.isEmpty() || timeStart.isBlank()){
                etTimeStart.setError("Time start cannot be empty");
                return false;
            }

            if (timeEnd.isEmpty()) {
                etTimeEnd.setError("Time end cannot be empty");
                return false;
            }
            if (cost.isEmpty()) {
                etCost.setError("Cost cannot be empty");
                return false;
            }

            try {
                LocalTime timeStarT = LocalTime.parse(timeStart);
                LocalTime timeEndT = LocalTime.parse(timeEnd);
                if (!timeStarT.isBefore(timeEndT)) {
                    etTimeStart.setError("Time start must be earlier than time end");
                    etTimeEnd.setError("Time end must be later than time start");
                    return false;
                }
            } catch (DateTimeParseException e) {
                etTimeStart.setError("Invalid time format (HH:mm)");
                etTimeEnd.setError("Invalid time format (HH:mm)");
                return false;
                }
            }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_add_slot){
            AddSlotView();
        }else if(id == R.id.btn_done){
            AddSlots();
        }
    }
}

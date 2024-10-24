package Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;

import java.util.List;

import Models.CourtSlot;

public class CourtSlotAdapter extends RecyclerView.Adapter<CourtSlotAdapter.CourtViewHolder> {

    private List<CourtSlot> courtSlots;

    public CourtSlotAdapter(List<CourtSlot> courtSlots){
        this.courtSlots = courtSlots;
    }


    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_time_item_layout, parent, false);
        return new CourtSlotAdapter.CourtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
        CourtSlot courtSlot = courtSlots.get(position);

        holder.tvSlot.setText("Slot " + (position + 1));
        holder.tvSlotTime.setText(courtSlot.getTimeStart() + " - " + courtSlot.getTimeEnd());

    }

    @Override
    public int getItemCount() {
        return courtSlots.size();
    }


    public static class CourtViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvSlot;
        private final TextView tvSlotTime;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSlot  = itemView.findViewById(R.id.tv_slot);
            tvSlotTime = itemView.findViewById(R.id.tv_slot_time);
        }
    }
}

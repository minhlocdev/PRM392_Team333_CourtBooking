package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.court_manage.EditCourt;

import java.util.List;

import Models.Court;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {

    private final Context context;
    private final List<Court> courts;
    private final int idLayout;

    public CourtAdapter(Context context, List<Court> courts, int idLayout) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
    }

    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new CourtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
        Court court = courts.get(position);

        // Set court name, open/close time, and status
        holder.tvCourtName.setText(court.getCourtName());
        holder.tvDescription.setText(court.getOpenTime() + " - " + court.getClosedTime());
        holder.tvStatus.setText(court.getStatus());

        // Set court image
        byte[] courtImage = court.getImage();
        if (courtImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(courtImage, 0, courtImage.length);
            holder.ivImage.setImageBitmap(bitmap);
        } else {
            holder.ivImage.setImageResource(R.drawable.old_trafford);
        }

        // Handle edit and delete button clicks
        holder.ibEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCourt.class);
            intent.putExtra("court_id", courts.get(position).getCourtId());
            context.startActivity(intent);
        });

        holder.ibDelete.setOnClickListener(v -> {
            // Handle delete action
        });
    }

    @Override
    public int getItemCount() {
        return courts.size();
    }

    // ViewHolder class to hold the view references
    public static class CourtViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvCourtName;
        TextView tvDescription;
        TextView tvStatus;
        ImageButton ibEdit;
        ImageButton ibDelete;
        RelativeLayout relativeLayout;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.court_title);
            tvDescription = itemView.findViewById(R.id.court_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ibEdit = itemView.findViewById(R.id.ib_edit);
            ibDelete = itemView.findViewById(R.id.ib_delete);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}

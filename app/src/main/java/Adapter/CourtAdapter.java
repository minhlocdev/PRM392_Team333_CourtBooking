package Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.EditCourt;
import java.util.List;
import Models.Court;
import Repository.CourtRepository;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {

    private final Context context;
    private final List<Court> courts;
    private final int idLayout;
    private final CourtRepository repository;


    public CourtAdapter(Context context, List<Court> courts, int idLayout) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
        this.repository = new CourtRepository(context);
    }

    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new CourtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
        Court court = courts.get(position);

        // Set court name, open/close time, and status
        holder.tvCourtName.setText(court.getCourtName());
        holder.tvDescription.setText(court.getOpenTime() + " - " + court.getClosedTime());
        holder.tvStatus.setText(court.getStatus());

        // Use more visually appealing colors
        if ("INACTIVE".equals(court.getStatus())) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FF4444")); // Bright red for closed courts
            holder.tvStatus.setTextColor(Color.WHITE); // White text for contrast
            holder.tvStatus.setPadding(8, 8, 8, 8); // Add padding for better readability
            holder.ibReactive.setVisibility(View.VISIBLE);
            holder.ibDelete.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green for open courts
            holder.tvStatus.setTextColor(Color.WHITE); // White text for contrast
            holder.tvStatus.setPadding(8, 8, 8, 8); // Add padding for better readability
            holder.ibReactive.setVisibility(View.GONE);
            holder.ibDelete.setVisibility(View.VISIBLE);
        }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this court?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int courtId = courts.get(position).getCourtId();
                CourtRepository courtRepo = new CourtRepository(context);

                // Update status to closed first
                int updateResult = courtRepo.deleteCourt(courtId);

                if (updateResult > 0) {
                    Toast.makeText(context, "Court closed successfully", Toast.LENGTH_SHORT).show();
                    // Optionally refresh the list or update the UI to reflect the change
                    // For example, you might want to update the court's status in your local list if needed
                    courts.get(position).setStatus("INACTIVE");

                    notifyItemChanged(position);

                } else {
                    Toast.makeText(context, "Failed to close court", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        holder.ibReactive.setOnClickListener(v -> {
            int courtId = courts.get(position).getCourtId();

            CourtRepository courtRepo = new CourtRepository(context);
            int updateResult = courtRepo.reactiveCourt(courtId);

            if (updateResult > 0) {
                Toast.makeText(context, "Court reactivate successfully", Toast.LENGTH_SHORT).show();

                courts.get(position).setStatus("ACTIVE");
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Failed to reactivate court", Toast.LENGTH_SHORT).show();
            }

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

        ImageButton ibReactive;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.court_title);
            tvDescription = itemView.findViewById(R.id.court_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ibEdit = itemView.findViewById(R.id.ib_edit);
            ibDelete = itemView.findViewById(R.id.ib_delete);
            ibReactive = itemView.findViewById(R.id.ib_reactivate);
        }
    }
}

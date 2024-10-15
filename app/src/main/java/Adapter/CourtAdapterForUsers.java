package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player_search.BookingDialog;
import java.util.List;
import Models.Court;

public class CourtAdapterForUsers extends RecyclerView.Adapter<CourtAdapterForUsers.CourtViewHolder> {

    private final Context context;
    private final List<Court> courts;
    private final int idLayout;
    private final FragmentManager fragmentManager;

    public CourtAdapterForUsers(Context context, List<Court> courts, int idLayout, FragmentManager fragmentManager) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CourtAdapterForUsers.CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new CourtAdapterForUsers.CourtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourtAdapterForUsers.CourtViewHolder holder, int position) {
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

        holder.btnBook.setOnClickListener(v -> {
            BookingDialog bookingDialog = new BookingDialog();
            Bundle args = new Bundle();
            args.putInt("courtId", courts.get(position).getCourtId());
            bookingDialog.setArguments(args);

            bookingDialog.show(fragmentManager, "BookingDialog");
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
        RelativeLayout relativeLayout;
        Button btnBook;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.court_title);
            tvDescription = itemView.findViewById(R.id.court_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            btnBook = itemView.findViewById(R.id.btn_booking);
        }
    }
}

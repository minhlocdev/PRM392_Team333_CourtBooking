package Adapter;

import static Constant.SessionConstant.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.court_owner.feedback.CourtFeedbackCourtOwner;

import java.util.List;

import Models.Court;
import Repository.BookingRepository;
import Repository.CourtOwnerRepository;
import Session.SessionManager;

public class CourtAdapterForHomePage extends RecyclerView.Adapter<CourtAdapterForHomePage.CourtViewHolder> {
    private final List<Court> courts;
    private final int idLayout;
    private final FragmentManager fragmentManager;
    private final Context context;
    private final BookingRepository bookingRepository;
    private final SessionManager sessionManager;
    private final CourtOwnerRepository courtOwnerRepository;

    public CourtAdapterForHomePage(Context context, List<Court> courts, int idLayout, FragmentManager fragmentManager) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
        this.fragmentManager = fragmentManager;
        bookingRepository = new BookingRepository(context);
        sessionManager = new SessionManager(context, user);
        courtOwnerRepository = new CourtOwnerRepository(context);
    }

    @NonNull
    @Override
    public CourtAdapterForHomePage.CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new CourtAdapterForHomePage.CourtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourtAdapterForHomePage.CourtViewHolder holder, int position) {
        Court court = courts.get(position);

        // Set court name, open/close time, and status
        holder.tvCourtName.setText(court.getCourtName());
        holder.tvDescription.setText(court.getOpenTime() + " - " + court.getClosedTime());
        holder.tvAddress.setText(court.getAddress() + " - " + court.getProvince());

        // Set court image
        byte[] courtImage = court.getImage();
        if (courtImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(courtImage, 0, courtImage.length);
            holder.ivImage.setImageBitmap(bitmap);
        } else {
            holder.ivImage.setImageResource(R.drawable.old_trafford);
        }

        // Set feedback button click listener
        holder.btnFeedBack.setOnClickListener(v -> {
            // Create a new CourtFeedback fragment and pass court ID as argument
            CourtFeedbackCourtOwner courtFeedbackFragment = new CourtFeedbackCourtOwner();
            Bundle args = new Bundle();
            args.putInt("court_id", court.getCourtId());  // Assuming Court model has a getId() method
            courtFeedbackFragment.setArguments(args);

            // Replace current fragment with CourtFeedback fragment
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, courtFeedbackFragment);
            transaction.addToBackStack(null);  // Optional: add to back stack
            transaction.commit();
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
        TextView tvAddress;
        AppCompatButton btnFeedBack;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize view components based on the new layout structure
            ivImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.court_title);
            tvDescription = itemView.findViewById(R.id.court_description);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnFeedBack = itemView.findViewById(R.id.btn_review);
        }
    }
}

package Adapter;

import static Constant.SessionConstant.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.court_manage.CourtFeedback;
import com.example.prm392_team333_courtbooking.fragements.player_search.BookingDialog;
import java.util.List;
import Models.Court;
import Repository.BookingRepository;
import Session.SessionManager;

public class CourtAdapterForUsers extends RecyclerView.Adapter<CourtAdapterForUsers.CourtViewHolder> {

    private final List<Court> courts;
    private final int idLayout;
    private final FragmentManager fragmentManager;
    private final Context context;
    private final BookingRepository bookingRepository;
    private final SessionManager sessionManager;

    public CourtAdapterForUsers(Context context, List<Court> courts, int idLayout, FragmentManager fragmentManager) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
        this.fragmentManager = fragmentManager;
        bookingRepository = new BookingRepository(context);
        sessionManager = new SessionManager(context, user);
    }

    @NonNull
    @Override
    public CourtAdapterForUsers.CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new CourtAdapterForUsers.CourtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourtAdapterForUsers.CourtViewHolder holder, int position) {
        Court court = courts.get(position);

        // Set court name, open/close time, and status
        holder.tvCourtName.setText(court.getCourtName());
        holder.tvDescription.setText(court.getOpenTime() + " - " + court.getClosedTime());
        holder.tvStatus.setText(court.getStatus());
        holder.tvAddress.setText(court.getAddress() + " - " + court.getProvince());

        int color = ContextCompat.getColor(context, R.color.booked);

        if(court.getStatus().equals("CLOSED")){
            color = ContextCompat.getColor(context, R.color.crimson);
        }

        holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));

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

        holder.btnReview.setOnClickListener(v -> {

            boolean hasBookingCompleted = bookingRepository.hasCompletedBooking(sessionManager.getUserId(), court.getCourtId());

            CourtFeedback courtFeedback = new CourtFeedback();
            Bundle args = new Bundle();

            if(hasBookingCompleted){
                args.putString("mode", "enable");
            }else{
                args.putString("mode", "disable");
            }

            courtFeedback.setArguments(args);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, courtFeedback)
                    .addToBackStack(null)
                    .commit();

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
        TextView tvAddress;
        RelativeLayout relativeLayout;
        Button btnBook, btnReview;

        public CourtViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.court_title);
            tvDescription = itemView.findViewById(R.id.court_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            btnBook = itemView.findViewById(R.id.btn_booking);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnReview = itemView.findViewById(R.id.btn_review);
        }
    }
}

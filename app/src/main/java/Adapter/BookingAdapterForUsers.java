package Adapter;

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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.Interface.BookingDialogListener;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.player.player_search.BookingDetail;

import java.util.List;
import Models.Booking;
import Models.Court;
import Repository.CourtRepository;

public class BookingAdapterForUsers extends RecyclerView.Adapter<BookingAdapterForUsers.BookingViewHolder> {

    private List<Booking> bookings;
    private final int idLayout;
    private final FragmentManager fragmentManager;
    private final CourtRepository courtRepository;
    private final BookingDialogListener listener;

    private Context context;

    public BookingAdapterForUsers(Context context, List<Booking> bookings, int idLayout, FragmentManager fragmentManager, BookingDialogListener listener) {
        this.context = context;
        this.bookings = bookings;
        this.idLayout = idLayout;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        courtRepository = new CourtRepository(context);
    }

    @NonNull
    @Override
    public BookingAdapterForUsers.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new BookingAdapterForUsers.BookingViewHolder(view);
    }

    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Booking booking = bookings.get(position);

        Court court = courtRepository.getCourtById(booking.getCourtId());

        byte[] courtImage = court.getImage();
        if (courtImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(courtImage, 0, courtImage.length);
            holder.ivCourtImage.setImageBitmap(bitmap);
        } else {
            holder.ivCourtImage.setImageResource(R.drawable.old_trafford);
        }

        holder.tvCourtName.setText(court.getCourtName());
        holder.tvDate.setText(booking.getBookingDate());
        holder.tvTime.setText(booking.getStartTime() + " - " + booking.getEndTime());
        holder.tvStatus.setText(booking.getStatus());
        holder.tvPrice.setText("Total : " + booking.getPrice());


        switch (booking.getStatus()){
            case "BOOKED":
            {
                int color = ContextCompat.getColor(context, R.color.olive_drab);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
            case "CANCEL":
            {
                int color = ContextCompat.getColor(context, R.color.crimson);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
            case "PENDING":
            {
                int color = ContextCompat.getColor(context, R.color.pending);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
            case "COMPLETED":
            {
                int color = ContextCompat.getColor(context, R.color.booked);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
            case "REFUSED":
            {
                int color = ContextCompat.getColor(context, R.color.dark_orange);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
        }

        holder.btnDetail.setOnClickListener( v -> {
                BookingDetail bookingDetail = new BookingDetail();
                Bundle args = new Bundle();
                args.putInt("bookingId", bookings.get(position).getBookingId());
                bookingDetail.setArguments(args);
                bookingDetail.setListener(listener);
                bookingDetail.show(fragmentManager, "BookingDetail");
        });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // ViewHolder class to hold the view references
    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivCourtImage;
        private final TextView tvCourtName;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvStatus;
        private final TextView tvPrice;
        private final Button btnDetail;


        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCourtImage = itemView.findViewById(R.id.court_image);
            tvCourtName = itemView.findViewById(R.id.tv_court_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}

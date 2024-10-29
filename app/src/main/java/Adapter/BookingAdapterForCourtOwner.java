package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.fragements.court_owner.court_manage.DeleteDialog;

import java.util.List;
import Models.Booking;
import Models.Court;
import Models.User;
import Repository.BookingRepository;
import Repository.CourtRepository;
import Repository.UserRepository;

public class BookingAdapterForCourtOwner extends RecyclerView.Adapter<BookingAdapterForCourtOwner.BookingViewHolder> {

    private final Context context;
    private List<Booking> bookings;
    private final FragmentManager fragmentManager;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingAdapterForCourtOwner(Context context, List<Booking> bookings, FragmentManager fragmentManager) {
        this.context = context;
        this.bookings = bookings;
        this.fragmentManager = fragmentManager;
        this.courtRepository = new CourtRepository(context);
        this.userRepository = new UserRepository(context);
        bookingRepository = new BookingRepository(context);
    }


    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingAdapterForCourtOwner.BookingViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        User user = userRepository.getUserById(booking.getPlayerId());
        Court court= courtRepository.getCourtById(booking.getCourtId());

        holder.tvCourtName.setText(court.getCourtName());
        holder.tvUserName.setText(user.getFullName());
        holder.tvDate.setText(booking.getBookingDate());
        holder.tvTime.setText(booking.getStartTime() + " - " + booking.getEndTime());
        holder.tvStatus.setText(booking.getStatus());
        holder.tvPrice.setText(String.valueOf(booking.getPrice()));

        switch (booking.getStatus()){
            case "BOOKED":
            {
                int color = ContextCompat.getColor(context, R.color.olive_drab);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefuse.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.VISIBLE);
                break;
            }
            case "CANCEL":
            {
                int color = ContextCompat.getColor(context, R.color.crimson);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefuse.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            }
            case "PENDING":
            {
                int color = ContextCompat.getColor(context, R.color.pending);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnRefuse.setVisibility(View.VISIBLE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            }
            case "COMPLETED":
            {
                int color = ContextCompat.getColor(context, R.color.booked);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefuse.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            }
            case "REFUSED":
            {
                int color = ContextCompat.getColor(context, R.color.dark_orange);
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefuse.setVisibility(View.GONE);
                holder.btnCompleted.setVisibility(View.GONE);
                break;
            }
        }

        byte[] avatar = user.getAvatar();
        if (avatar != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            holder.ivAvatar.setImageBitmap(bitmap);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ten_hag);
        }

        holder.btnAccept.setOnClickListener(v -> {
            bookingRepository.updateBooking(booking.getBookingId(), booking.getCourtId(), booking.getPlayerId(), booking.getBookingDate(), booking.getStartTime(),
                    booking.getEndTime(), (float) booking.getPrice(), "BOOKED", "");
            notifyDataSetChanged();
        });

        holder.btnRefuse.setOnClickListener(v -> {
            DeleteDialog dialog = new DeleteDialog(booking.getBookingId(), (reason, bookingId) -> {
                updateBookingStatusToRefused(bookingId, reason);
            });
            dialog.show(fragmentManager, "RefuseBookingDialog");
        });


        holder.btnCompleted.setOnClickListener(v -> {
            bookingRepository.updateBooking(booking.getBookingId(), booking.getCourtId(), booking.getPlayerId(), booking.getBookingDate(), booking.getStartTime(),
                    booking.getEndTime(), (float) booking.getPrice(), "COMPLETED", "");
            notifyDataSetChanged();
        });

    }

    public void setBookingList(List<Booking> bookings){
        this.bookings = bookings;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateBookingStatusToRefused(int bookingId, String reason) {
        // You can now update the booking status and handle any additional logic
        // Example: calling a repository method to update the booking in the database
        Booking booking = bookingRepository.getBookingsById(bookingId);
        if (booking != null) {
            booking.setStatus("REFUSED");
            booking.setReason(reason);
            bookingRepository.updateBooking(booking.getBookingId(), booking.getCourtId(), booking.getPlayerId(),
                    booking.getBookingDate(), booking.getStartTime(), booking.getEndTime(),
                    (float) booking.getPrice(), booking.getStatus(), booking.getReason());
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // ViewHolder class to hold the view references
    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivAvatar;
        private final TextView tvUserName, tvCourtName;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvStatus;
        private final TextView tvPrice;
        private final Button btnAccept;
        private final Button btnRefuse;
        private final Button btnCompleted;


        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.avatar);
            tvCourtName = itemView.findViewById(R.id.tv_court_name);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnRefuse = itemView.findViewById(R.id.btn_refused);
            btnCompleted = itemView.findViewById(R.id.btn_completed);
        }
    }
}

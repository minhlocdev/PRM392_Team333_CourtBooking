    package ListAdapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.prm392_team333_courtbooking.R;

    import java.util.List;

    import Models.Court;

    public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {

        private Context context;
        private List<Court> courtList;

        // Constructor for CourtAdapter
        public CourtAdapter(Context context, List<Court> courtList) {
            this.context = context;
            this.courtList = courtList;
        }

        @NonNull
        @Override
        public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the court_item layout for each item
            View view = LayoutInflater.from(context).inflate(R.layout.court_list_search_view, parent, false);
            return new CourtViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
            // Get the current court from the list
            Court court = courtList.get(position);

            // Set the hardcoded image for Old Trafford
            holder.courtImage.setImageResource(R.drawable.old_trafford);  // Assuming this drawable exists

            // Set the court name, location, and status
            holder.courtName.setText(court.getCourtName());
            holder.courtLocation.setText(court.getLocation());
            holder.courtStatus.setText(court.getStatus());
        }

        @Override
        public int getItemCount() {
            return courtList.size();
        }

        // ViewHolder class to hold item views
        public static class CourtViewHolder extends RecyclerView.ViewHolder {

            ImageView courtImage;
            TextView courtName;
            TextView courtLocation;
            TextView courtStatus;

            public CourtViewHolder(@NonNull View itemView) {
                super(itemView);
                courtImage = itemView.findViewById(R.id.courtImage);
                courtName = itemView.findViewById(R.id.courtName);
                courtLocation = itemView.findViewById(R.id.courtLocation);
                courtStatus = itemView.findViewById(R.id.courtStatus); // Assuming status is displayed in the UI
            }
        }
    }

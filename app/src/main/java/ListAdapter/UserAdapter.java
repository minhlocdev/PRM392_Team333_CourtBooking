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

import Models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;

    // Constructor to initialize the list of users
    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_player_search_account_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fullName.setText(user.getFullName());

        // You can set a placeholder avatar here, or load an image if the avatar is provided
        holder.avatarImage.setImageResource(R.drawable.ten_hag); // Replace with actual image if available
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder to hold UI components for each user item
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImage;
        TextView fullName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            fullName = itemView.findViewById(R.id.follower_fullname);
        }
    }
}

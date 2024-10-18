/*
package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private final List<User> users;
    private final int idLayout;

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.idLayout = R.layout.fragment_player_search_account_item;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        // Set user name, email, phone, and status
        holder.tvFullName.setText(user.getFullName());

         //If you have a user image, decode and set it here
         //Assuming getImage() method in User to get image bytes (if needed)
         byte[] userImage = user.getImage();
         if (userImage != null) {
             Bitmap bitmap = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
             holder.ivAvatar.setImageBitmap(bitmap);
         } else {
             holder.ivAvatar.setImageResource(R.drawable.ten_hag); // Replace with your default image
         }



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvFullName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.avatar_image);  // Matching the ID in your XML layout
            tvFullName = itemView.findViewById(R.id.follower_fullname);  // Matching the ID in your XML layout
        }
    }

}
*/

package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import java.util.List;
import Models.Reply;
import Models.User;
import Repository.UserRepository;


public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private final Context context;
    private final List<Reply> replies;
    private final UserRepository userRepository;

    public ReplyAdapter(Context context, List<Reply> replies) {
        this.context = context;
        this.replies = replies;
        userRepository = new UserRepository(context);
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.replies_court_layout, parent, false);
        return new ReplyAdapter.ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replies.get(position);
        User user = userRepository.getUserById(reply.getUserId());

        holder.tvContent.setText(reply.getContent());
        holder.tvDate.setText(reply.getCreateAt());
        holder.tvUserName.setText(user.getFullName());
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUserName;
        private final TextView tvDate;
        private final TextView tvContent;

        public ReplyViewHolder(@NonNull View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tv_reply_user);
            tvDate = view.findViewById(R.id.tv_date);
            tvContent = view.findViewById(R.id.tv_reply_content);
        }
    }
}

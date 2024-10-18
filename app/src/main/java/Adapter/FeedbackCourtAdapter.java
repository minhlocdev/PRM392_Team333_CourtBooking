package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import java.util.List;

import Models.Reply;
import Models.Review;
import Models.User;
import Repository.ReplyRepository;
import Repository.UserRepository;

public class FeedbackCourtAdapter extends RecyclerView.Adapter<FeedbackCourtAdapter.FeedbackViewHolder> {

    private final Context context;
    private final List<Review> reviews;
    private final FragmentManager fragmentManager;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    public FeedbackCourtAdapter(Context context, List<Review> reviews,  FragmentManager fragmentManager) {
        this.context = context;
        this.reviews = reviews;
        this.fragmentManager = fragmentManager;
        userRepository = new UserRepository(context);
        replyRepository = new ReplyRepository(context);

    }

    @NonNull
    @Override
    public FeedbackCourtAdapter.FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new FeedbackCourtAdapter.FeedbackViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Review review = reviews.get(position);

        User user = userRepository.getUserById(review.getUserId());

        holder.tvFeedBackUser.setText(user.getFullName());
        holder.tvFeedBackContent.setText(review.getContent());
        holder.tvFeedBackDate.setText(review.getCreateAt());

        List<Reply> replies = replyRepository.getAllRepliesByReviewId(review.getReviewId());

        ReplyAdapter adapter = new ReplyAdapter(context, replies);

        holder.rvReplies.setLayoutManager(new LinearLayoutManager(context));
        holder.rvReplies.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFeedBackUser, tvFeedBackDate, tvFeedBackContent;
        private RecyclerView rvReplies;

        public FeedbackViewHolder(@NonNull View view) {
            super(view);

            tvFeedBackUser = view.findViewById(R.id.tv_feedback_user);
            tvFeedBackDate = view.findViewById(R.id.tv_feedback_date);
            tvFeedBackContent = view.findViewById(R.id.tv_feedback_content);
            rvReplies = view.findViewById(R.id.rv_replies_list);

        }
    }
}

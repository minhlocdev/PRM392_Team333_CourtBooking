package Adapter;

import static Constant.SessionConstant.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;

import java.time.LocalDate;
import java.util.List;

import Models.Booking;
import Models.Reply;
import Models.Review;
import Models.User;
import Repository.ReplyRepository;
import Repository.UserRepository;
import Session.SessionManager;

public class FeedbackCourtAdapter extends RecyclerView.Adapter<FeedbackCourtAdapter.FeedbackViewHolder> {

    private final Context context;
    private List<Review> reviews;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final SessionManager sessionManager;

    public FeedbackCourtAdapter(Context context, List<Review> reviews,  FragmentManager fragmentManager) {
        this.context = context;
        this.reviews = reviews;
        userRepository = new UserRepository(context);
        replyRepository = new ReplyRepository(context);
        sessionManager = new SessionManager(context, user);
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

        holder.tvReply.setOnClickListener(v -> {

            LayoutInflater inflater = LayoutInflater.from(context);
            View replyView = inflater.inflate(R.layout.reply_layout, null); // replace with the actual layout name


            EditText etReply = replyView.findViewById(R.id.et_reply);
            Button btnPost = replyView.findViewById(R.id.btn_post);


            if (holder.replyContainer.getChildCount() == 0) {
                holder.replyContainer.addView(replyView);
            }


            btnPost.setOnClickListener(view -> {
                String replyText = etReply.getText().toString().trim();
                if (replyText.isEmpty() || replyText.isBlank()) {
                    etReply.setError("Reply content is required");
                    etReply.requestFocus();
                    return;
                }

                    int replyId = (int)replyRepository.insertReply(review.getReviewId(), sessionManager.getUserId(), replyText, LocalDate.now().toString(), "NEW");

                    Reply reply = replyRepository.getReplyById(replyId);

                    replies.add(reply);

                    adapter.notifyItemInserted(replies.size() - 1);
                    holder.rvReplies.scrollToPosition(replies.size() - 1);


                    etReply.setText("");
                    holder.replyContainer.removeView(replyView);

            });
        });

    }

    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFeedBackUser;
        private final TextView tvFeedBackDate;
        private final TextView tvFeedBackContent;
        private final RecyclerView rvReplies;
        private final TextView tvReply;
        private final LinearLayout replyContainer;

        public FeedbackViewHolder(@NonNull View view) {
            super(view);

            tvFeedBackUser = view.findViewById(R.id.tv_feedback_user);
            tvFeedBackDate = view.findViewById(R.id.tv_feedback_date);
            tvFeedBackContent = view.findViewById(R.id.tv_feedback_content);
            rvReplies = view.findViewById(R.id.rv_replies_list);
            tvReply = view.findViewById(R.id.tv_reply);
            replyContainer = view.findViewById(R.id.replyContainer);

        }
    }
}

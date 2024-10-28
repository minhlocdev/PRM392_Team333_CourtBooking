package com.example.prm392_team333_courtbooking.fragements.court_owner.feedback;

import static Constant.SessionConstant.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import com.example.prm392_team333_courtbooking.activities.owner.court_owner_layout;

import java.time.LocalDate;
import java.util.List;
import Adapter.FeedbackCourtAdapter;
import Models.Review;
import Repository.ReviewRepository;
import Session.SessionManager;

public class CourtFeedback extends Fragment {

    private EditText etReview;

    private ReviewRepository reviewRepository;

    private SessionManager sessionManager;

    private FeedbackCourtAdapter adapter;

    private int courtId;
    @Override
    public void onResume() {
        super.onResume();
        ((court_owner_layout) requireActivity()).updateToolbarTitle("Court Feedback"); // Set the title for this fragment
        ((court_owner_layout) requireActivity()).setNavigationButtonEnabled(true);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.court_feedback_layout, container, false);

        etReview = view.findViewById(R.id.et_review);
        Button btnPost = view.findViewById(R.id.btn_post);

        sessionManager = new SessionManager(requireContext(), user);

        RecyclerView rvReviews = view.findViewById(R.id.rv_feedback_list);

        etReview.setMovementMethod(new ScrollingMovementMethod());
        etReview.setVerticalScrollBarEnabled(true);

        reviewRepository = new ReviewRepository(requireContext());

        String mode = "disabled";

        if (getArguments() != null) {
            mode = getArguments().getString("mode");
        }


        if (getArguments() != null) {
            courtId = getArguments().getInt("courtId");
        }


        ReviewRepository reviewRepository = new ReviewRepository(requireContext());

        List<Review> reviews = reviewRepository.getAllReviewsByCourtId(courtId);

        if(mode != null && mode.equals("disable")){
            etReview.setFocusable(false);
            etReview.setClickable(false);
            etReview.setCursorVisible(false);
            etReview.setLongClickable(false);

            btnPost.setEnabled(false);


            adapter = new FeedbackCourtAdapter(requireContext(), reviews, getParentFragmentManager(), "disable");
        }else{
            adapter = new FeedbackCourtAdapter(requireContext(), reviews, getParentFragmentManager(), "enable");

        }

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setAdapter(adapter);

        btnPost.setOnClickListener(v -> post());
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void post(){
        String review = etReview.getText().toString().trim();

        if(review.isEmpty() || review.isBlank()){
            etReview.setError("Review is empty");
            etReview.requestFocus();
            return;
        }

       reviewRepository.insertReview(sessionManager.getUserId(), courtId, 4, review, LocalDate.now().toString(), "NEW");

        etReview.setText("");

        List<Review> reviews = reviewRepository.getAllReviewsByCourtId(courtId);
        adapter.setReviews(reviews);
        adapter.notifyDataSetChanged();
    }
}

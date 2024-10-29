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

public class CourtFeedbackCourtOwner extends Fragment {

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

        View view = inflater.inflate(R.layout.court_feedback_layout_court_owner, container, false);

        SessionManager sessionManager = new SessionManager(requireContext(), user);

        RecyclerView rvReviews = view.findViewById(R.id.rv_feedback_list);

        ReviewRepository reviewRepository1 = new ReviewRepository(requireContext());

        if (getArguments() != null) {
            courtId = getArguments().getInt("courtId");
        }

        ReviewRepository reviewRepository = new ReviewRepository(requireContext());

        List<Review> reviews = reviewRepository.getAllReviewsByCourtId(courtId);


        FeedbackCourtAdapter adapter = new FeedbackCourtAdapter(requireContext(), reviews, getParentFragmentManager(), "enable");

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setAdapter(adapter);

        return view;
    }
}

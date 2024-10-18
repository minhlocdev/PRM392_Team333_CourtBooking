package com.example.prm392_team333_courtbooking.court_manage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_team333_courtbooking.R;
import java.util.List;
import Adapter.FeedbackCourtAdapter;
import Models.Review;
import Repository.ReviewRepository;

public class CourtFeedback extends Fragment {

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.court_feedback_layout, container, false);

        RecyclerView rvReviews = view.findViewById(R.id.rv_feedback_list);

        String mode = "disabled";

        if (getArguments() != null) {
            mode = getArguments().getString("edit_mode");
        }

        int courtId = 0;

        if (getArguments() != null) {
            courtId = getArguments().getInt("courtId");
        }


        ReviewRepository reviewRepository = new ReviewRepository(requireContext());

        List<Review> reviews = reviewRepository.getAllReviewsByCourtId(courtId);

        FeedbackCourtAdapter adapter = new FeedbackCourtAdapter(requireContext(), reviews, getParentFragmentManager());

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setAdapter(adapter);

        return view;
    }
}

package com.example.prm392_team333_courtbooking.fragements.court_owner.feedback;

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
import Adapter.ReplyAdapter;
import Models.Reply;
import Repository.ReplyRepository;

public class Replies extends Fragment {

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.court_feedback_layout, container, false);

        RecyclerView rvReviews = view.findViewById(R.id.rv_feedback_list);

        int replyId = 0;

        if (getArguments() != null) {
            replyId = getArguments().getInt("replyId");
        }


        ReplyRepository replyRepository = new ReplyRepository(requireContext());

        List<Reply> replies = replyRepository.getAllRepliesByReviewId(replyId);

        ReplyAdapter adapter = new ReplyAdapter(requireContext(), replies);

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setAdapter(adapter);

        return view;
    }
}

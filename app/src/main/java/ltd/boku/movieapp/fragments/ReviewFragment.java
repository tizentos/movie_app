package ltd.boku.movieapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.adapters.ReviewRecyclerViewAdapter;
import ltd.boku.movieapp.database.Review;
import ltd.boku.movieapp.viewmodels.ReviewViewModel;

public class ReviewFragment extends Fragment {

    public ReviewRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private RecyclerView reviewRecyclerView;
    ReviewViewModel reviewViewModel;
    private static final String TAG = "ReviewFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: entering");
        View view=inflater.inflate(R.layout.reviews_layout,container,false);
        mReviewRecyclerViewAdapter=new ReviewRecyclerViewAdapter(getContext());

        reviewViewModel= ViewModelProviders.of(this).get(ReviewViewModel.class);
        reviewViewModel.getReviewList().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                mReviewRecyclerViewAdapter.setReviews(reviews);
            }
        });
        reviewRecyclerView=view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerViewAdapter=new ReviewRecyclerViewAdapter(getContext());
        reviewRecyclerView.setAdapter(mReviewRecyclerViewAdapter);
        return view;
    }
}

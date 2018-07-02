package ltd.boku.movieapp.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.adapters.ReviewRecyclerViewAdapter;
import ltd.boku.movieapp.viewmodels.ReviewViewModel;

public class ReviewFragment extends Fragment {

    public ReviewRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private RecyclerView reviewRecyclerView;
    ReviewViewModel reviewViewModel;

    public static ProgressBar reviewsProgressBar;
    public TextView noReviewTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.reviews_layout,container,false);
        reviewsProgressBar=view.findViewById(R.id.reviews_progressbar);
        noReviewTextView =view.findViewById(R.id.no_review_text_view);


        mReviewRecyclerViewAdapter=new ReviewRecyclerViewAdapter(getContext());

        reviewViewModel= ViewModelProviders.of(this).get(ReviewViewModel.class);
        reviewViewModel.getReviewList().observe(this, reviews -> {
            mReviewRecyclerViewAdapter.setReviews(reviews);
            if (reviews.isEmpty()) {
                noReviewTextView.setVisibility(View.VISIBLE);
            } else{
                noReviewTextView.setVisibility(View.GONE);
            }
        });

        reviewRecyclerView=view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setAdapter(mReviewRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL);

        reviewRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }
}

package ltd.boku.movieapp.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.adapters.TrailerRecyclerViewAdapter;
import ltd.boku.movieapp.utilities.AppUtility;
import ltd.boku.movieapp.viewmodels.TrailerViewModel;

public class TrailerFragment extends Fragment implements TrailerRecyclerViewAdapter.OnTrailerClickListener{

    public TrailerRecyclerViewAdapter mTrailerRecyclerViewAdapter;
    private RecyclerView trailerRecyclerView;
    TrailerViewModel trailerViewModel;

    public static  ProgressBar trailerProgressBar;
    TextView noTrailerTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.trailers_layout,container,false);
        trailerProgressBar=view.findViewById(R.id.trailer_progressbar);
        trailerProgressBar.setVisibility(View.VISIBLE);
        noTrailerTextView=view.findViewById(R.id.no_trailer_text_view);

        mTrailerRecyclerViewAdapter=new TrailerRecyclerViewAdapter(this);

        trailerViewModel= ViewModelProviders.of(this).get(TrailerViewModel.class);
        trailerViewModel.getTrailerList().observe(this, trailers -> {

            mTrailerRecyclerViewAdapter.setTrailers(trailers);
            if (trailers.isEmpty()) {
                noTrailerTextView.setVisibility(View.VISIBLE);
            } else{
                noTrailerTextView.setVisibility(View.GONE);
            }
        });

        trailerRecyclerView=view.findViewById(R.id.trailer_recycler);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setAdapter(mTrailerRecyclerViewAdapter);


        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL);

        trailerRecyclerView.addItemDecoration(dividerItemDecoration);

        trailerProgressBar.setVisibility(View.GONE);

        return view;
    }


    @Override
    public void onTrailerClickListener(String trailerId) {
//        Toast.makeText(getContext(),"Getting video key : "+trailerId,Toast.LENGTH_LONG).show();
        Intent intent= YouTubeStandalonePlayer.createVideoIntent(getActivity(), AppUtility.GOOGLEAPI,trailerId,0,true,false);
        startActivity(intent);
    }
}



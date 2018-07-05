package ltd.boku.movieapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.database.Trailer;

import static ltd.boku.movieapp.utilities.AppUtility.GOOGLEAPI;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.TrailerViewHolder> {

    OnTrailerClickListener context;
    List<Trailer> Trailers =new ArrayList<>();

    public interface OnTrailerClickListener{
        void onTrailerClickListener(String trailerId);
    }
    public TrailerRecyclerViewAdapter(OnTrailerClickListener context) {
        this.context = context;
    }

    public TrailerRecyclerViewAdapter(OnTrailerClickListener context, List<Trailer> trailers) {
        this.context = context;
        this.Trailers=trailers;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.trailer_layout,parent,false);
        TrailerViewHolder trailerViewHolder=new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer bindTrailer=new Trailer(Trailers.get(position).getTrailerId(),Trailers.get(position).getTrailerTitle());


        holder.trailerTitle.setText(bindTrailer.getTrailerTitle().toString());

        holder.youtubeTrailerThumbnail.initialize(GOOGLEAPI, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(bindTrailer.getTrailerId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });

        holder.itemView.setOnClickListener(v -> context.onTrailerClickListener(bindTrailer.getTrailerId()));

    }

    @Override
    public int getItemCount() {
        if (Trailers == null) return 0;
        return Trailers.size();
    }

    public  void setTrailers(List<Trailer> trailers){
        Trailers = trailers;
        notifyDataSetChanged();
    }

    public static  class TrailerViewHolder extends RecyclerView.ViewHolder{

        TextView trailerTitle;
        YouTubeThumbnailView youtubeTrailerThumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerTitle =itemView.findViewById(R.id.textview_trailer);
            youtubeTrailerThumbnail=itemView.findViewById(R.id.youTubeThumbnailView);
        }
    }
}

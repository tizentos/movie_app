package ltd.boku.movieapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.database.Movie;



public class MovieRecyclerViewAdapter extends  RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder>{

    public  interface  OnMovieRecyclerViewClickListener{
        void onMovieRecyclerViewClickListener(int itemId);
    }

    //define dataset and context for the adapter
    static OnMovieRecyclerViewClickListener context;
    List<Movie> movieList;

    public MovieRecyclerViewAdapter(OnMovieRecyclerViewClickListener context) {
        this.context = context;
    }

    //set new movie list
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    //get a movie from the movie list
    public Movie getMovie(int position){
        return movieList.get(position);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        MovieViewHolder movieViewHolder=new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movie movie=movieList.get(position);

        Picasso.with((Context)context)
                .load(Movie.baseImageUrl+movie.getImage_thumbnail_url())
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        if (movieList != null){
            return movieList.size();
        }
        return 0;
    }

    public static  class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView movieImage;
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage=itemView.findViewById(R.id.img_movie);
            //send click signal to context
            movieImage.setOnClickListener(v -> {
                int index=getAdapterPosition();
                context.onMovieRecyclerViewClickListener(index);
            });
        }
    }
}

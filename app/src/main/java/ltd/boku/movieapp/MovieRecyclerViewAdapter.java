package ltd.boku.movieapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;



public class MovieRecyclerViewAdapter extends  RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder>{

    interface  OnMovieRecyclerViewClickListener{
        void onMovieRecyclerViewClickListener(int itemId);
    }

    //define dataset and context for the adapter
    static OnMovieRecyclerViewClickListener context;
    ltd.boku.movieapp.Movie[] movieList;

    public MovieRecyclerViewAdapter(OnMovieRecyclerViewClickListener context) {
        this.context = context;
    }

    //set new movie list
    public void setMovieList(Movie[] movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    //get a movie from the movie list
    public Movie getMovie(int position){
        return movieList[position];
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

        ltd.boku.movieapp.Movie movie=movieList[position];

        Picasso.with((Context)context)
                .load(movie.getImage_thumbnail_url())
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        if (movieList != null){
            return movieList.length;
        }
        return 0;
    }

    public static  class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView movieImage;
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage=itemView.findViewById(R.id.img_movie);
            //send click signal to context
            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=getAdapterPosition();
                    context.onMovieRecyclerViewClickListener(index);
                }
            });
        }
    }
}

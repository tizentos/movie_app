package ltd.boku.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {

    //define UI component
    private ImageView imageView;
    private TextView txTitle;
    private TextView txRelease;
    private TextView txOverview;
    private RatingBar userRating;

    private static  String MOVIE_EXTRA="movie";

    private Movie movie=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imageView=findViewById(R.id.img_movie);
        txTitle=findViewById(R.id.tx_movie_title);
        txRelease=findViewById(R.id.tx_release_date);
        txOverview=findViewById(R.id.tx_overview);
        userRating=findViewById(R.id.ratingBar);

        userRating.setMax(5);
        userRating.setNumStars(5);
        userRating.setStepSize(0.2f);

        Intent intent=getIntent();

        if (intent.hasExtra(MOVIE_EXTRA)){
            movie=(Movie)intent.getSerializableExtra(MOVIE_EXTRA);
        } else{
            finish();
        }

        Picasso.with(this).load(movie.getImage_thumbnail_url())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        txTitle.setText(movie.getOriginal_title());
        txRelease.setText(movie.getRelease_date());
        txOverview.setText(movie.getOverview());
        userRating.setRating(movie.getUser_rating()/2);

        setTitle("Movie Detail");

    }
}

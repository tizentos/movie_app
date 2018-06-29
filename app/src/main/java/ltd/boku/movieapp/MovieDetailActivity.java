package ltd.boku.movieapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ltd.boku.movieapp.database.Movie;
import ltd.boku.movieapp.database.Review;
import ltd.boku.movieapp.utilities.AppExecutors;
import ltd.boku.movieapp.utilities.AppUtility;
import ltd.boku.movieapp.viewmodels.ReviewViewModel;
import ltd.boku.movieapp.viewpagerAdapter.TabViewPagerAdapter;

import static ltd.boku.movieapp.utilities.AppUtility.BASEURL;


public class MovieDetailActivity extends AppCompatActivity {

    //define UI component
    private ImageView imageView;
    private TextView txTitle;
    private TextView txRelease;
    private TextView txOverview;
    private RatingBar userRating;

    private static  String MOVIE_EXTRA="movie";

    public static  Movie movie=null;
    public static  List<Review> reviews=new ArrayList<>();
    public List<String> trailers=new ArrayList<>();

//    public  ReviewRecyclerViewAdapter mReviewRecyclerViewAdapter;
//    private RecyclerView reviewRecyclerView;
    private ViewPager tabViewPager;
    private TabViewPagerAdapter mTabViewPagerAdapter;
    private TabLayout tabLayout;

    private static final String TAG = "MovieDetailActivity";

    ReviewViewModel reviewViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent=getIntent();

        if (intent.hasExtra(MOVIE_EXTRA)){
            movie=(Movie)intent.getSerializableExtra(MOVIE_EXTRA);
        } else{
            finish();
        }

        imageView=findViewById(R.id.img_movie);
        txTitle=findViewById(R.id.tx_movie_title);
        txRelease=findViewById(R.id.tx_release_date);
        txOverview=findViewById(R.id.tx_overview);
        userRating=findViewById(R.id.ratingBar);

        userRating.setMax(5);
        userRating.setNumStars(5);
        userRating.setStepSize(0.2f);
//        mReviewRecyclerViewAdapter=new ReviewRecyclerViewAdapter(this);

//        reviewViewModel= ViewModelProviders.of(this).get(ReviewViewModel.class);
//        reviewViewModel.getReviewList().observe(this, new Observer<List<Review>>() {
//            @Override
//            public void onChanged(@Nullable List<Review> reviews) {
//                mReviewRecyclerViewAdapter.setReviews(reviews);
//            }
//        });

//
//        reviewRecyclerView=findViewById(R.id.reviewRecyclerView);
//        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        reviewRecyclerView.setHasFixedSize(true);
//        mReviewRecyclerViewAdapter=new ReviewRecyclerViewAdapter(this);
//        reviewRecyclerView.setAdapter(mReviewRecyclerViewAdapter);

        tabViewPager=findViewById(R.id.viewpager);
        mTabViewPagerAdapter =new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPager.setAdapter(mTabViewPagerAdapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(tabViewPager);


        Picasso.with(this).load(movie.getImage_thumbnail_url())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        txTitle.setText(movie.getOriginal_title());
        txRelease.setText(movie.getRelease_date());
        txOverview.setText(movie.getOverview());
        userRating.setRating(movie.getUser_rating()/2);

        setTitle("Movie Detail");
    }

    public void loadTrailers(){
        URL trailerURL=AppUtility.composeOtherURL(BASEURL,movie.id,"videos");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String trailerJSON=AppUtility.getResponseFromHttpUrl(trailerURL);
                    trailers=AppUtility.getTrailer(trailerJSON);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

}

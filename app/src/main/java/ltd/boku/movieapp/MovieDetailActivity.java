package ltd.boku.movieapp;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimationUtilsCompat;
import android.support.transition.Slide;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ltd.boku.movieapp.adapters.TrailerRecyclerViewAdapter;
import ltd.boku.movieapp.database.Movie;
import ltd.boku.movieapp.database.Review;
import ltd.boku.movieapp.database.Trailer;
import ltd.boku.movieapp.utilities.AppExecutors;
import ltd.boku.movieapp.utilities.AppUtility;
import ltd.boku.movieapp.utilities.FavoriteMovieRepository;
import ltd.boku.movieapp.viewmodels.ReviewViewModel;
import ltd.boku.movieapp.viewpagerAdapter.TabViewPagerAdapter;

import static ltd.boku.movieapp.MainActivity.ONFAVORITE;
import static ltd.boku.movieapp.utilities.AppUtility.BASEURL;


public class MovieDetailActivity extends AppCompatActivity{

    private static final String TAG = "MovieDetailActivity";

    //define UI component
    private ImageView imageView;
    private TextView txTitle;
    private TextView txRelease;
    private TextView txOverview;
    private RatingBar userRating;

//    Tab layout initialization
    private ViewPager tabViewPager;
    private TabViewPagerAdapter mTabViewPagerAdapter;
    private TabLayout tabLayout;

    //Menu
    MenuItem item;


    //movie instance
    public static  Movie movie=null;
    //Intent data key
    private static  String MOVIE_EXTRA="movie";

    //intent flag
    private boolean onFavorite=false;

    FavoriteMovieRepository favoriteMovieRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent=getIntent();



        if ((intent.hasExtra(MOVIE_EXTRA)) && (intent.hasExtra(ONFAVORITE))){
            movie=(Movie)intent.getSerializableExtra(MOVIE_EXTRA);
            onFavorite=intent.getBooleanExtra(ONFAVORITE,false);
        } else{
            finish();
        }

        imageView=findViewById(R.id.img_movie);
        txTitle=findViewById(R.id.tx_movie_title);
        txRelease=findViewById(R.id.tx_release_date);
        txOverview=findViewById(R.id.tx_overview);
        userRating=findViewById(R.id.ratingBar);


        //TODO receiving activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            android.transition.Slide slide = new android.transition.Slide(Gravity.BOTTOM);
            slide.addTarget(txTitle);
            slide.addTarget(imageView);
            slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.fast_out_slow_in));
            slide.setDuration(300);
//            getWindow().setEnterTransition(slide);

            // check below for shared element transition
             getWindow().setSharedElementEnterTransition(new AutoTransition());
        }



        userRating.setMax(5);
        userRating.setNumStars(5);
        userRating.setStepSize(0.2f);


        tabViewPager=findViewById(R.id.viewpager);
        mTabViewPagerAdapter =new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPager.setAdapter(mTabViewPagerAdapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(tabViewPager);


        Picasso.with(this).load(Movie.baseImageUrl+movie.getImage_thumbnail_url())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        txTitle.setText(movie.getOriginal_title());
        txRelease.setText(movie.getRelease_date());
        txOverview.setText(movie.getOverview());
        userRating.setRating(movie.getUser_rating()/2);


        favoriteMovieRepository=new FavoriteMovieRepository(this);
        setTitle("Movie Detail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu,menu);
        item=menu.findItem(R.id.mark_favorite_menu);
        setFavoriteIcon(onFavorite);
        return true;
    }

    public void setFavoriteIcon(boolean onFavorite){
        if (onFavorite){
            item.setIcon(R.drawable.ic_favorite_black_48dp);
        }else{
            item.setIcon(R.drawable.ic_favorite_border_black_48dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.mark_favorite_menu:
                Log.d(TAG, "onOptionsItemSelected: favorite menu clicked");
                if (item.isChecked()){
                    Log.d(TAG, "onOptionsItemSelected: checked");
                    boolean isAdded= (movie != null) && favoriteMovieRepository.insertFavoriteMovie(movie);
                   // item.setChecked(isAdded);
                    item.setIcon(R.drawable.ic_favorite_black_48dp);
                }else{
                    Log.d(TAG, "onOptionsItemSelected: unchecked");
                    boolean isRemoved= (movie != null) && favoriteMovieRepository.deleteFavoriteMovie(movie);
                   // item.setChecked(!isRemoved);
                    item.setIcon(R.drawable.ic_favorite_border_black_48dp);
                }
                item.setChecked(!item.isChecked());
                return true;
            case R.id.menu_share:
                shareMovie();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back pressed");
        finish();
    }

    public void shareMovie(){
        String mimeType="text/plain";

        String title=getString(R.string.share_title);

        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(getString(R.string.share_message,movie.getOriginal_title()))
                .startChooser();
    }
}

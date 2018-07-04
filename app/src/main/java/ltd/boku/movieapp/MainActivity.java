package ltd.boku.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.stetho.Stetho;

import java.io.IOException;

import java.io.Serializable;
import java.net.URL;
import java.util.List;


import ltd.boku.movieapp.adapters.MovieRecyclerViewAdapter;
import ltd.boku.movieapp.database.Movie;
import ltd.boku.movieapp.utilities.AppUtility;
import ltd.boku.movieapp.utilities.FavoriteMovieRepository;
import ltd.boku.movieapp.viewmodels.MainViewModel;

import static ltd.boku.movieapp.utilities.AppUtility.BASEURL;

public class MainActivity extends AppCompatActivity implements  MovieRecyclerViewAdapter.OnMovieRecyclerViewClickListener {

    //Progress bar
    ProgressBar progressBar;

    //savedInstanceState elements and intent extras
    private static String MOVIE_EXTRA = "movie";
    public static final String MOVIELIST_INSTANCE = "MOVIE";
    public static final String TITLE = "TITLE";
    public static String PATH="popular";   //to save temp path
    public static final String ONFAVORITE="favorite";

    //define working parameters
    public static String title = "Popular movies";
    public String movieJSON = null;
    public static List<Movie> movieList;
    private  Movie movie = new Movie();

    //Recycler view definitions
    static MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private static  int currentRecyclerPosition=0;


    //Room database repository
    FavoriteMovieRepository favoriteMovieRepository;


    //flags
    static boolean onFavorite=false;

    //View model
    MainViewModel mainViewModel;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: entering");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.main_screen_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        //Stetho debugger
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());


        URL url = AppUtility.composeURL(BASEURL, PATH);
        Log.d(TAG, "onCreate: "+ url);

        if (savedInstanceState != null) {
            title=savedInstanceState.getString(TITLE);
            onFavorite=savedInstanceState.getBoolean(ONFAVORITE,false);
            movieList=(List<Movie>)savedInstanceState.getSerializable(MOVIELIST_INSTANCE);
            currentRecyclerPosition=savedInstanceState.getInt("position");
        }

        Log.d(TAG, "onCreate: " + onFavorite);
        mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovies().observe(this, movies -> movieRecyclerViewAdapter.setMovieList(movies));

        if (checkConnection() && !onFavorite){
            favoriteMovieRepository=new FavoriteMovieRepository(this);
            Log.d("Check", "onCreate: not on favorite" + onFavorite);
            loadMovie(url);
        } else if (onFavorite){
            favoriteMovieRepository=new FavoriteMovieRepository(this);
            favoriteMovieRepository.getFavoriteMovieList().observe(this, movies -> movieList=movies);
            mainViewModel.setMovies(movieList);
        }
        configureRecycler();
        setTitle(title);
        progressBar.setVisibility(View.GONE);
    }

    private void configureRecycler() {
        recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.getLayoutManager().scrollToPosition(currentRecyclerPosition);


        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        progressBar.setVisibility(View.GONE);
        Log.d(TAG, "configureRecycler: "+ currentRecyclerPosition);
    }

    public boolean checkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info=connectivityManager.getAllNetworkInfo();

        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        }

        Snackbar.make(findViewById(R.id.main_screen),
                "Internet not connected",Snackbar.LENGTH_INDEFINITE)
                .setAction("RECONNECT", v -> reconnect()).show();
        progressBar.setVisibility(View.GONE);

        return false;
    }
    void reconnect(){
       boolean connected=checkConnection();
       if (connected){
           URL url = AppUtility.composeURL(BASEURL, PATH);
           loadMovie(url);
           return;
       }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: entering");
        if (title!=null) {
            outState.putString(TITLE, title);
            outState.putBoolean(ONFAVORITE,onFavorite);
        } if (movieList != null){
            outState.putSerializable(MOVIELIST_INSTANCE,(Serializable)movieList);
        }
        outState.putInt("position",currentRecyclerPosition);
        super.onSaveInstanceState(outState);
    }


    private void loadMovie(URL composedURL) {
        getMovies movies = new getMovies();
        movies.execute(composedURL);
    }


    @Override
    public void onMovieRecyclerViewClickListener(int itemId) {
        movie = movieRecyclerViewAdapter.getMovie(itemId);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);
        intent.putExtra(ONFAVORITE,onFavorite);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        URL composedURL;

        switch (id) {
            case R.id.menu_popular:
                PATH="popular";
                composedURL = AppUtility.composeURL(BASEURL, PATH);
                title="Popular Movies";
                this.setTitle(title);
                if (checkConnection()) {
                    loadMovie(composedURL);
                }
                mainViewModel.setMovies(movieList);
                onFavorite=false;
                return true;
            case R.id.menu_top_rated:
                PATH="top_rated";
                composedURL = AppUtility.composeURL(BASEURL, PATH);
                title="Top-Rated Movies";
                this.setTitle(title);
                if (checkConnection()) {
                    loadMovie(composedURL);
                }
                mainViewModel.setMovies(movieList);
                onFavorite=false;
                return true;
            case R.id.menu_show_favorite:
                progressBar.setVisibility(View.VISIBLE);
                favoriteMovieRepository.getFavoriteMovieList()
                        .observe(this, movies -> {
                            movieRecyclerViewAdapter.setMovieList(movies);
                            movieList=movies;
                            mainViewModel.setMovies(movieList);
                            mainViewModel.getMovies().observe(this, mainMovies -> movieRecyclerViewAdapter.setMovieList(mainMovies));
                        });
                //Log.d(TAG, "onOptionsItemSelected: movies number" + movieList.size());
                progressBar.setVisibility(View.GONE);
                title="Favorites";
                this.setTitle(title);
                onFavorite=true;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Async class
    class getMovies extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse JSON
            AppUtility.movieJSONParser(s,MainActivity.movieList,MainActivity.movieRecyclerViewAdapter);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            Log.d(TAG, "doInBackground: " + url);
            runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
            try {
                movieJSON = AppUtility.getResponseFromHttpUrl(url);
                return movieJSON;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: entering");
        if (savedInstanceState != null) {
            title=savedInstanceState.getString(TITLE);
            onFavorite=savedInstanceState.getBoolean(ONFAVORITE,false);
            movieList=(List<Movie>)savedInstanceState.getSerializable(MOVIELIST_INSTANCE);
            currentRecyclerPosition=savedInstanceState.getInt("position");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: entering");

        currentRecyclerPosition= ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

        Log.d(TAG, "onPause: " + currentRecyclerPosition);
    }
}


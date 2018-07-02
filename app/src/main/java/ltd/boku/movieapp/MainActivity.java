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
    public static String title = null;
    public String movieJSON = null;
    public static List<Movie> movieList;
    private  Movie movie = new Movie();

    //Recycler view definitions
    static MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private RecyclerView recyclerView;


    //Room database repository
    FavoriteMovieRepository favoriteMovieRepository;


    //flags
    boolean  onFavorite=false;

    //View model
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.main_screen_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        //Stetho debugger
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());


        URL url = AppUtility.composeURL(BASEURL, PATH);
        title="Popular movies";    //default view

        if (savedInstanceState != null) {
            title=savedInstanceState.getString(TITLE);
            onFavorite=savedInstanceState.getBoolean(ONFAVORITE,false);
        }

        if (checkConnection() && !onFavorite){
            Log.d("Check", "onCreate: not on favorite" + onFavorite);
            loadMovie(url);
        }


        mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovies().observe(this, movies -> movieRecyclerViewAdapter.setMovieList(movies));


        configureRecycler();



        setTitle(title);
        favoriteMovieRepository=new FavoriteMovieRepository(this);
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


        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        progressBar.setVisibility(View.GONE);
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
        if (title!=null) {
            outState.putString(TITLE, title);
            outState.putBoolean(ONFAVORITE,onFavorite);
        }
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
                        });
                progressBar.setVisibility(View.GONE);
                title="Favorites";
                this.setTitle(title);
                mainViewModel.setMovies(movieList);
                mainViewModel.getMovies().observe(this, movies -> movieRecyclerViewAdapter.setMovieList(movies));
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
}


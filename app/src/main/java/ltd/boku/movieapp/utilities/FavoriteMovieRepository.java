package ltd.boku.movieapp.utilities;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ltd.boku.movieapp.database.AppDatabase;
import ltd.boku.movieapp.database.Movie;
import ltd.boku.movieapp.database.MovieDao;
import ltd.boku.movieapp.database.Review;
import ltd.boku.movieapp.database.ReviewDao;

public class FavoriteMovieRepository {

    private MovieDao movieDao;
    private ReviewDao reviewDao;

    private LiveData<Movie> favoriteMovie;
    private LiveData<List<Movie>> favoriteMovieList;

    private LiveData<List<Review>> favoriteMovieReviews;


    public FavoriteMovieRepository(Context context){
        AppDatabase appDatabase=AppDatabase.getsInstance(context);
        movieDao=appDatabase.movieDao();
        reviewDao=appDatabase.reviewDao();
    }

    /**
     * insert operation for movie table
     * @param movie
     */
    public boolean insertFavoriteMovie(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.insert(movie);
            }
        });
        return  true;
    }

    public void updateFavoriteMovie(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.update(movie);
            }
        });
    }
    public boolean deleteFavoriteMovie(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieDao.delete(movie);
            }
        });
        return true;
    }

    public LiveData<List<Movie>> getFavoriteMovieList(){
       return movieDao.findAllMovies();
    }


    //Reserved methods
    public LiveData<Movie> getFavoriteMovie(long id){
        return movieDao.findMovieById(id);
    }

    public LiveData<List<Review>> getFavoriteMovieReviews(long favoriteMovieId){
        return reviewDao.findReviewForMovie(favoriteMovieId);
    }


}

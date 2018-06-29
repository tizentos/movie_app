package ltd.boku.movieapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(Review review);

    @Update
    void update(Review... reviews);

    @Delete
    void delete(Review... reviews);

    @Query("SELECT * FROM review")
    LiveData<List<Review>> getAllReviews();

    @Query("SELECT * FROM review WHERE movie_id=:movieId")
    LiveData<List<Review>> findReviewForMovie(long movieId);

}

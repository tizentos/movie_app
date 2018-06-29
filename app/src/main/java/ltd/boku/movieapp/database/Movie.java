package ltd.boku.movieapp.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.io.Serializable;

@Entity(tableName = "movie")
public class Movie implements Serializable {
    private static final String TAG = "Movie";
    private static final long serialVersionUID=1L;

    @PrimaryKey
    public long id;

    @Ignore
    public static  String baseImageUrl= "http://image.tmdb.org/t/p/w342";

    public String original_title;
    public String image_thumbnail_url;
    public String overview;
    public float user_rating;
    public String release_date;
    public String reviews;
    public String keys;

    @Ignore
    public Movie(){ }


    public Movie(long id, String original_title, String image_thumbnail_url, String overview, float user_rating, String release_date) {
        this.id = id;
        this.original_title = original_title;
        this.image_thumbnail_url = image_thumbnail_url;
        this.overview = overview;
        this.user_rating = user_rating;
        this.release_date = release_date;
    }

    public long getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getImage_thumbnail_url() {
        return image_thumbnail_url;
    }

    public String getOverview() {
        return overview;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }




    public void setId(long id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setImage_thumbnail_url(String image_thumbnail_url) {
        this.image_thumbnail_url = image_thumbnail_url;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}

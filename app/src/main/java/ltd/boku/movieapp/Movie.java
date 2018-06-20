package ltd.boku.movieapp;


import android.util.Log;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final String TAG = "Movie";
    private static final long serialVersionUID=1L;

    private static  String baseImageUrl= "http://image.tmdb.org/t/p/w342";
    private String original_title;
    private String image_thumbnail_url;
    private String overview;
    private float user_rating;
    private String release_date;

    public Movie(){ }

    public Movie(String original_title, String image_thumbnail_url, String overview, String user_rating, String release_date) {
        this.original_title = original_title;
        this.image_thumbnail_url = baseImageUrl+image_thumbnail_url;
        this.overview = overview;
        this.user_rating =Float.parseFloat(user_rating);
        this.release_date = release_date;
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
}

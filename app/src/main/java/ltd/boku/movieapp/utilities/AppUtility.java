package ltd.boku.movieapp.utilities;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ltd.boku.movieapp.MainActivity;
import ltd.boku.movieapp.Movie;
import ltd.boku.movieapp.MovieRecyclerViewAdapter;

public class AppUtility {


    //buid a URL with a specific path
    public static URL composeURL(String baseUrl, String path) {
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path)
                .appendQueryParameter("api_key", MainActivity.APIKEY)
                .appendQueryParameter("page", "1")
                .appendQueryParameter("language", "en-US")
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //handle http request and response
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        int res = urlConnection.getResponseCode();
        try {
            BufferedReader inputStreamResponse = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while (null != (line = inputStreamResponse.readLine())) {
                result.append(line).append("\n");
            }
            return result.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    //parse received JSON data
    public static void movieJSONParser(String rawJSON, Movie[] movieList, MovieRecyclerViewAdapter movieRecyclerViewAdapter) {
        String original_title = null;
        String image_thumbnail_url = null;
        String overview = null;
        String user_rating = null;
        String release_date = null;
        Movie movie = null;
        try {
            JSONObject jsonObject = new JSONObject(rawJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            movieList = new Movie[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                original_title = jsonItem.getString("original_title");
                image_thumbnail_url = jsonItem.getString("poster_path");
                overview = jsonItem.getString("overview");
                user_rating = jsonItem.getString("vote_average");
                release_date = jsonItem.getString("release_date");
                movie = new Movie(original_title, image_thumbnail_url, overview, user_rating, release_date);
                movieList[i] = movie;
            }
            movieRecyclerViewAdapter.setMovieList(movieList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

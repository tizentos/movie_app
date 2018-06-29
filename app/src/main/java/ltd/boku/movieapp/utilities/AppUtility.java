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
import java.util.ArrayList;
import java.util.List;

import ltd.boku.movieapp.database.Movie;
import ltd.boku.movieapp.adapters.MovieRecyclerViewAdapter;
import ltd.boku.movieapp.database.Review;


public class AppUtility {
    public static final String APIKEY = "API";
    public static final String BASEURL = "https://api.themoviedb.org/3/movie";

    //buid a URL with a specific path
    public static URL composeURL(String baseUrl, String path) {
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path)
                .appendQueryParameter("api_key", APIKEY)
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

    public static  URL composeOtherURL(String baseUrl,long id, String otherInfo){
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(otherInfo)
                .appendQueryParameter("api_key", APIKEY)
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
        long movieId=0;
        String original_title = null;
        String image_thumbnail_url = null;
        String overview = null;
        float user_rating = 0f;
        String release_date = null;
        Movie movie = null;
        try {
            JSONObject jsonObject = new JSONObject(rawJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            movieList = new Movie[jsonArray.length()];

//            String jsonArr=jsonArray.toString(1);
//            Log.d("JSON Converter", "movieJSONParser: " + jsonArr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                movieId=jsonItem.getLong("id");
                original_title = jsonItem.getString("original_title");
                image_thumbnail_url = jsonItem.getString("poster_path");
                overview = jsonItem.getString("overview");
                user_rating = Float.parseFloat(jsonItem.getString("vote_average"));
                release_date = jsonItem.getString("release_date");
                movie = new Movie(movieId,original_title, image_thumbnail_url, overview, user_rating, release_date);
                movieList[i] = movie;
            }
            movieRecyclerViewAdapter.setMovieList(movieList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static  String reviewJSONToString(String rawJSON){
        String JSONResult=null;

        try{
            JSONObject jsonObject=new JSONObject(rawJSON);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            JSONResult=jsonArray.toString(1);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return JSONResult;
    }

    public static List<Review> reviewJSONToReviewArray(String rawJSON){
        String author=null;
        String content=null;
        String id=null;
        List<Review> reviews=new ArrayList<>();
        try{
            JSONObject jsonObject=new JSONObject(rawJSON);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            if (jsonArray.length() > 0) {
//                reviews=new ArrayList<>(jsonArray.length());
//                reviews=new Review[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem=jsonArray.getJSONObject(i);
                    author=jsonItem.getString("author");
                    content=jsonItem.getString("content");
                    id=jsonItem.getString("id");
                    reviews.add(new Review(id,author,content));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return reviews;
    }

    public static List<String> getTrailer(String rawJSON){
        List<String> key=new ArrayList<>();
        try{
            JSONObject jsonObject=new JSONObject(rawJSON);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem=jsonArray.getJSONObject(i);
                    key.add(jsonItem.getString("key"));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return key;
    }
//xxxx9ed859bdd4440d4271aef4ede997b7c4xxxx//
}

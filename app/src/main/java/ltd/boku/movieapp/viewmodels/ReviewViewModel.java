package ltd.boku.movieapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ltd.boku.movieapp.MovieDetailActivity;
import ltd.boku.movieapp.database.Review;
import ltd.boku.movieapp.fragments.ReviewFragment;
import ltd.boku.movieapp.utilities.AppUtility;

import static ltd.boku.movieapp.MovieDetailActivity.movie;
import static ltd.boku.movieapp.utilities.AppUtility.BASEURL;


public class ReviewViewModel extends ViewModel {

    MutableLiveData<List<Review>> reviewList;
    List<Review> reviews=new ArrayList<>();



    public LiveData<List<Review>> getReviewList(){
        if (reviewList == null){
            reviewList= new MutableLiveData<List<Review>>();
            URL reviewURL= AppUtility.composeOtherURL(BASEURL,movie.id,"reviews");
            new loadReviews().execute(reviewURL);
        }
        return reviewList;
    }

    public class loadReviews extends AsyncTask<URL, Void, List<Review>>{

        @Override
        protected void onPostExecute(List<Review> listLiveData) {
            super.onPostExecute(listLiveData);
            ReviewFragment.reviewsProgressBar.setVisibility(View.GONE);
            reviewList.setValue(listLiveData);
        }

        @Override
        protected List<Review> doInBackground(URL... urls) {
            ReviewFragment.reviewsProgressBar.setVisibility(View.VISIBLE);
            URL url=urls[0];
            List<Review> reviews= new ArrayList<>();
            try {
                String reviewJSON=AppUtility.getResponseFromHttpUrl(url);
                reviews=AppUtility.reviewJSONToReviewArray(reviewJSON);
            } catch (IOException e){
                e.printStackTrace();
            }
            return reviews;
           // return reviewList;
        }
    }
}

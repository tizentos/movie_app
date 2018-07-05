package ltd.boku.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import ltd.boku.movieapp.MovieDetailActivity;
import ltd.boku.movieapp.database.Trailer;
import ltd.boku.movieapp.fragments.TrailerFragment;
import ltd.boku.movieapp.utilities.AppUtility;

import static ltd.boku.movieapp.MovieDetailActivity.movie;
import static ltd.boku.movieapp.utilities.AppUtility.BASEURL;

public class TrailerViewModel extends ViewModel {

    MutableLiveData<List<Trailer>> trailerList;
    List<Trailer> trailers=new ArrayList<>();


    public LiveData<List<Trailer>> getTrailerList(){
        if (trailerList==null){
            trailerList=new MutableLiveData<>();
            URL trailerURL= AppUtility.composeOtherURL(BASEURL,movie.id,"videos");
            new loadTrailers().execute(trailerURL);
        }

        return  trailerList;
    }

    public class loadTrailers extends AsyncTask<URL,Void,List<Trailer>>{
        @Override
        protected void onPreExecute() {
            TrailerFragment.trailerProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);

            TrailerFragment.trailerProgressBar.setVisibility(View.GONE);
            trailerList.setValue(trailers);
        }

        @Override
        protected List<Trailer> doInBackground(URL... urls) {
            URL url=urls[0];
            List<Trailer> trailers= new ArrayList<>();
            try {
                String trailerJSON=AppUtility.getResponseFromHttpUrl(url);
                trailers=AppUtility.getTrailer(trailerJSON);
            } catch (IOException e){
                e.printStackTrace();
            }
            return trailers;
        }
    }
}

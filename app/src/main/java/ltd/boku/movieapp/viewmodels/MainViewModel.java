package ltd.boku.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ltd.boku.movieapp.database.Movie;

public class MainViewModel extends ViewModel {
    public  MutableLiveData<List<Movie>> movieData=new MutableLiveData<>();

    public LiveData<List<Movie>> getMovies() {
        return movieData;
    }
    public  void setMovies(List<Movie> movies){
        if (movies != null) movieData.setValue(movies);
    }
}

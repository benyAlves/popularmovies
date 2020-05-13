package com.udacity.maluleque.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
    private LiveData<List<Movie>> allFavoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving from database");
        allFavoriteMovies = database.MovieDao().getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return allFavoriteMovies;
    }
}

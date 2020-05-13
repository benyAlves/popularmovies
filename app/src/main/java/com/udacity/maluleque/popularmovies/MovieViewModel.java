package com.udacity.maluleque.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.model.Movie;

class MovieViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public MovieViewModel(AppDatabase database, long movieId) {
        movie = database.MovieDao().findById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}

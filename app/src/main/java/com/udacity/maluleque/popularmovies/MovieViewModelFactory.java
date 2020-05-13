package com.udacity.maluleque.popularmovies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.maluleque.popularmovies.database.AppDatabase;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final long movieId;

    public MovieViewModelFactory(AppDatabase database, long movieId) {
        this.database = database;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(database, movieId);
    }
}
